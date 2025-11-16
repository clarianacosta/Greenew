package com.greenew.relatorios.service.impl;

import com.greenew.relatorios.config.client.ArvoresServiceClient;
import com.greenew.relatorios.config.client.ProdutoresServiceClient;
import com.greenew.relatorios.config.dto.ArvoreResponseDTO;
import com.greenew.relatorios.config.dto.TerrenoResponseDTO;
import com.greenew.relatorios.exception.RecursoNaoEncontradoException;
import com.greenew.relatorios.model.dto.RecomendacaoCompensacaoDTO;
import com.greenew.relatorios.model.dto.RecomendacaoRanqueadaDTO;
import com.greenew.relatorios.service.RecomendacaoService;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecomendacaoServiceImpl implements RecomendacaoService {

    private final ArvoresServiceClient arvoresServiceClient;
    private final ProdutoresServiceClient produtoresServiceClient;

    // Assumindo 1 Hectare = 10.000 m²
    private static final BigDecimal METROS_QUADRADOS_POR_HECTARE = new BigDecimal("10000");

    // Constantes de Normalização de Score

    /**
     * Define o teto de "excelência" para a absorção anual de CO2 (kg/ano).
     * Árvores com este valor ou superior recebem a pontuação máxima de absorção (1.0).
     * Valor estimado a partir das metodologias do Painel Intergovernamental sobre Mudanças
     * Climáticas (IPCC) para inventários de GEE (www.ipcc.ch).
     */
    private static final double MAX_ABSORCAO_ESPERADA_KG = 50.0;

    /**
     * Define o limite de "inviabilidade" para o custo por muda (R$).
     * Mudas com este custo ou superior recebem a pontuação mínima de custo (0.0).
     */
    private static final double MAX_CUSTO_ESPERADO_REAIS = 150.0;

    /**
     * Define o limite de "longo prazo" para a maturidade da árvore (anos).
     * Árvores que levam este tempo ou mais recebem a pontuação mínima de tempo (0.0).
     * Está alinhado com os horizontes de tempo de projetos de
     * carbono, que frequentemente usam períodos de 20-30 anos para
     * contabilizar o sequestro (conceitos de "Permanência" do GHG Protocol e padrões
     * de crédito de carbono como Verra/v-c-s.org).
     */
    private static final double MAX_TEMPO_ESPERADO_ANOS = 30.0;

    /**
     * Define a "nota 0.0" para a área ocupada por árvore (m²), incluindo espaçamento.
     * Árvores que ocupam esta área ou mais recebem pontuação mínima de espaço (0.0).
     * Eu optei por estimar a partir seguir dos princípios de espaçamento em silvicultura
     * e restauração encontrados online.
     */
    private static final double MAX_AREA_ESPERADA_M2 = 350.0;

    // Pesos que definem a importância de cada categoria no ranking final.
    private static final double PESO_ABSORCAO = 0.40;   // 40%
    private static final double PESO_CUSTO = 0.30;      // 30%
    private static final double PESO_TEMPO = 0.20;      // 20%
    private static final double PESO_ESPACO = 0.10;     // 10%

    public RecomendacaoServiceImpl(ArvoresServiceClient aClient, ProdutoresServiceClient pClient) {
        this.arvoresServiceClient = aClient;
        this.produtoresServiceClient = pClient;
    }

    @Override
    public List<RecomendacaoRanqueadaDTO> gerarRankingRecomendacoes(BigDecimal totalEmissoesCO2e) {

        // 1. Busca todos os dados de base
        List<ArvoreResponseDTO> todasAsArvores = arvoresServiceClient.buscarTodasArvores();
        List<TerrenoResponseDTO> todosOsTerrenos = produtoresServiceClient.buscarTodosTerrenos();

        if (todasAsArvores.isEmpty()) {
            throw new RecursoNaoEncontradoException("Nenhuma árvore cadastrada no sistema.");
        }

        // 2. Mapeia cada árvore para uma recomendação potencial
        List<RecomendacaoRanqueadaDTO> recomendacoes = todasAsArvores.stream()
                .map(arvore -> criarRecomendacaoParaArvore(arvore, totalEmissoesCO2e, todosOsTerrenos))
                .filter(recomendacao -> !recomendacao.getTerrenosCompatíveis().isEmpty())
                .sorted(Comparator.comparing(RecomendacaoRanqueadaDTO::getPontuacao).reversed()) // Ordena pela pontuação
                .limit(3) // Pega o Top 3
                .collect(Collectors.toList());

        // 3. Define os nomes dos Ranks
        if (recomendacoes.size() > 0) recomendacoes.get(0).setRank("Principal Recomendação");
        if (recomendacoes.size() > 1) recomendacoes.get(1).setRank("Alternativa 1");
        if (recomendacoes.size() > 2) recomendacoes.get(2).setRank("Alternativa 2");

        return recomendacoes;
    }

    private RecomendacaoRanqueadaDTO criarRecomendacaoParaArvore(ArvoreResponseDTO arvore, BigDecimal totalEmissoesKG, List<TerrenoResponseDTO> todosOsTerrenos) {


        // Calcula Quantidade e Custo
        BigDecimal absorcaoTotalPorArvore = arvore.getTaxaAbsorcaoCo2Anual().multiply(new BigDecimal(arvore.getTempoMaturidadeAnos()));

        long quantidadeNecessaria = absorcaoTotalPorArvore.compareTo(BigDecimal.ZERO) == 0 ? 0 :
                totalEmissoesKG.divide(absorcaoTotalPorArvore, 0, RoundingMode.CEILING).longValue();

        BigDecimal custoTotal = arvore.getCustoMedioMuda().multiply(new BigDecimal(quantidadeNecessaria));

        double areaPorArvoreM2 = calcularAreaPorArvore(arvore);
        BigDecimal areaTotalNecessariaM2 = new BigDecimal(areaPorArvoreM2 * quantidadeNecessaria);
        BigDecimal areaTotalNecessariaHectares = areaTotalNecessariaM2.divide(METROS_QUADRADOS_POR_HECTARE, 4, RoundingMode.HALF_UP);

        List<TerrenoResponseDTO> terrenosCompativeis = todosOsTerrenos.stream()
                .filter(terreno -> terrenoCompativel(terreno, arvore, areaTotalNecessariaHectares))
                .collect(Collectors.toList());

        // Monta o DTO de resposta
        RecomendacaoRanqueadaDTO dto = new RecomendacaoRanqueadaDTO();
        dto.setArvore(arvore);
        dto.setPontuacao(calcularPontuacaoFinal(arvore)); // A pontuação da árvore em si
        dto.setQuantidadeNecessaria(quantidadeNecessaria);
        dto.setCustoTotalEstimado(custoTotal);
        dto.setAreaTotalNecessariaHectares(areaTotalNecessariaHectares.doubleValue());
        dto.setTerrenosCompatíveis(terrenosCompativeis);

        return dto;
    }

    private boolean terrenoCompativel(TerrenoResponseDTO terreno, ArvoreResponseDTO arvore, BigDecimal areaTotalNecessariaHectares) {
        if (terreno.getBiomaLocal() == null || terreno.getClimaLocal() == null || terreno.getAreaDisponivelHectares() == null) {
            return false;
        }

        // 1. Filtro de Bioma e Clima
        boolean biomaCompativel = arvore.getBiomas().stream().anyMatch(b -> b.getId().equals(terreno.getBiomaLocal().getId()));
        boolean climaCompativel = arvore.getClimas().stream().anyMatch(c -> c.getId().equals(terreno.getClimaLocal().getId()));

        // 2. Filtro de Área (Requisito 1.b)
        // areaDisponivelHectares >= areaTotalNecessariaHectares
        boolean areaCompativel = terreno.getAreaDisponivelHectares().compareTo(areaTotalNecessariaHectares) >= 0;

        return biomaCompativel && climaCompativel && areaCompativel;
    }

    private double calcularAreaPorArvore(ArvoreResponseDTO arvore) {
        double raioCopa = arvore.getDiametroCopaMedioM().doubleValue() / 2.0;
        // Adiciona um fator de espaçamento (ex: 1.5x o raio) para desconsiderar o plantio de avrores coladas
        double raioComEspacamento = raioCopa * 1.5;
        return (Math.PI * raioComEspacamento * raioComEspacamento);
    }

    /**
     * Calcula a pontuação final de uma árvore, normalizando as métricas
     * para uma escala de 0.0 a 1.0 antes de aplicar os pesos.
     */
    private double calcularPontuacaoFinal(ArvoreResponseDTO arvore) {

        // 1. Score de Absorção (Direto: quanto maior, melhor)
        // Normaliza o score: (valor / max)
        double scoreAbsorcao = arvore.getTaxaAbsorcaoCo2Anual().doubleValue() / MAX_ABSORCAO_ESPERADA_KG;

        // 2. Scores Inversos (Inverso: quanto menor o valor, melhor o score)
        // Normaliza o score: (1 - (valor / max))
        double scoreCusto = 1.0 - (arvore.getCustoMedioMuda().doubleValue() / MAX_CUSTO_ESPERADO_REAIS);
        double scoreTempo = 1.0 - (arvore.getTempoMaturidadeAnos().doubleValue() / MAX_TEMPO_ESPERADO_ANOS);

        // Calcula a área da copa (com o fator de espaçamento de 1.5x o raio)
        double raioCopa = arvore.getDiametroCopaMedioM().doubleValue() / 2.0;
        double raioComEspacamento = raioCopa * 1.5;
        double areaCopaM2 = (Math.PI * raioComEspacamento * raioComEspacamento);

        // Normaliza o score de espaço
        double scoreEspaco = 1.0 - (areaCopaM2 / MAX_AREA_ESPERADA_M2);

        // 3. Trava de Segurança (Garante que scores não fiquem negativos)
        // Se um valor estoura o MAX, sua nota será 0, não negativa.
        scoreAbsorcao = Math.max(0, scoreAbsorcao);
        scoreCusto = Math.max(0, scoreCusto);
        scoreTempo = Math.max(0, scoreTempo);
        scoreEspaco = Math.max(0, scoreEspaco);

        // 4. Aplica os pesos
        return (scoreAbsorcao * PESO_ABSORCAO) +
                (scoreCusto * PESO_CUSTO) +
                (scoreTempo * PESO_TEMPO) +
                (scoreEspaco * PESO_ESPACO);
    }
}