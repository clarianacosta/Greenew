package com.greenew.relatorios.service.impl;

import com.greenew.relatorios.config.client.ArvoresServiceClient;
import com.greenew.relatorios.config.client.EmpresasServiceClient;
import com.greenew.relatorios.config.client.ProdutoresServiceClient;
import com.greenew.relatorios.config.dto.ArvoreResponseDTO;
import com.greenew.relatorios.config.dto.TerrenoResponseDTO;
import com.greenew.relatorios.exception.RecursoNaoEncontradoException;
import com.greenew.relatorios.model.dto.*;
import com.greenew.relatorios.model.entity.NivelCompletude;
import com.greenew.relatorios.model.entity.RelatorioGHGEntity;
import com.greenew.relatorios.model.mapper.RelatorioGHGMapper;
import com.greenew.relatorios.repository.RelatorioGHGRepository;
import com.greenew.relatorios.service.CalculoGHGService;
import com.greenew.relatorios.service.RecomendacaoService;
import com.greenew.relatorios.service.RelatorioGHGService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class RelatorioGHGServiceImpl implements RelatorioGHGService {

    private final RelatorioGHGRepository relatorioRepository;
    private final RelatorioGHGMapper relatorioMapper;
    private final CalculoGHGService calculoService;
    private final RecomendacaoService recomendacaoService;
    private final EmpresasServiceClient empresasServiceClient;
    private final ArvoresServiceClient arvoresServiceClient;
    private final ProdutoresServiceClient produtoresServiceClient;

    public RelatorioGHGServiceImpl(RelatorioGHGRepository rRepo, RelatorioGHGMapper rMapper, CalculoGHGService cService, RecomendacaoService recService, EmpresasServiceClient eClient, ArvoresServiceClient arvoresServiceClient, ProdutoresServiceClient produtoresServiceClient) {
        this.relatorioRepository = rRepo;
        this.relatorioMapper = rMapper;
        this.calculoService = cService;
        this.recomendacaoService = recService;
        this.empresasServiceClient = eClient;
        this.arvoresServiceClient = arvoresServiceClient;
        this.produtoresServiceClient = produtoresServiceClient;
    }

    @Override
    public RelatorioGHGResponseDTO criarRelatorio(RelatorioGHGRequestDTO requestDTO) {
        if (!empresasServiceClient.verificarSeEmpresaExiste(requestDTO.getEmpresaId())) {
            throw new RecursoNaoEncontradoException("Impossível criar relatório: Empresa não encontrada com ID " + requestDTO.getEmpresaId());
        }
        RelatorioGHGEntity entity = relatorioMapper.toEntity(requestDTO);
        return relatorioMapper.toResponseDTO(relatorioRepository.save(entity));
    }

//    @Override
//    public RelatorioGHGResponseDTO finalizarRelatorio(UUID relatorioId, UUID terrenoId, Set<Integer> escopos) {
//        RelatorioGHGEntity relatorio = relatorioRepository.findById(relatorioId)
//                .orElseThrow(() -> new RecursoNaoEncontradoException("Relatório não encontrado: " + relatorioId));
//
//        BigDecimal totalEmissoes = calculoService.calcularEmissoes(relatorio, escopos);
//        relatorio.setEmissaoCalculadaCo2e(totalEmissoes);
//        relatorio.setNivel(escopos.contains(3) ? NivelCompletude.COMPLETO : NivelCompletude.OPERACIONAL);
//
//        if (totalEmissoes.compareTo(BigDecimal.ZERO) > 0) {
//            RecomendacaoCompensacaoDTO recomendacao = recomendacaoService.gerarRecomendacao(totalEmissoes, terrenoId);
//            relatorio.setArvoreRecomendada(recomendacao.getArvoreRecomendada());
//            relatorio.setQuantidadeNecessaria(recomendacao.getQuantidadeNecessaria());
//            relatorio.setCustoTotalEstimado(recomendacao.getCustoTotalEstimado());
//        }
//
//        return relatorioMapper.toResponseDTO(relatorioRepository.save(relatorio));
//    }

    @Override
    @Transactional(readOnly = true)
    public RelatorioGHGResponseDTO buscarPorId(UUID relatorioId) {
        RelatorioGHGEntity relatorio = relatorioRepository.findById(relatorioId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Relatório não encontrado: " + relatorioId));

        return hidratarDTOComTerrenos(relatorio);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RelatorioGHGResponseDTO> buscarTodosPorEmpresaId(UUID empresaId) {
        // 1. Usa o repositório para buscar todas as entidades de relatório para a empresa
        List<RelatorioGHGEntity> relatoriosDaEmpresa = relatorioRepository.findAllByEmpresaId(empresaId);

        // 2. Converte a lista de entidades para uma lista de DTOs
        return relatoriosDaEmpresa.stream()
                .map(this::hidratarDTOComTerrenos)
                .collect(Collectors.toList());
    }

    private RelatorioGHGResponseDTO hidratarDTOComTerrenos(RelatorioGHGEntity entity) {
        // 1. Mapeamento básico
        RelatorioGHGResponseDTO dto = relatorioMapper.toResponseDTO(entity);

        // 2. Verifica se uma árvore já foi atribuída
        if (entity.getArvoreIdRecomendada() != null) {
            try {
                // 3. Busca a árvore
                ArvoreResponseDTO arvore = arvoresServiceClient.buscarArvorePorId(entity.getArvoreIdRecomendada());

                // 4. Calcula a área necessária
                double areaM2 = recomendacaoService.calcularAreaPorArvore(arvore);
                BigDecimal areaHectares = new BigDecimal(areaM2 * entity.getQuantidadeNecessaria())
                        .divide(RecomendacaoService.METROS_QUADRADOS_POR_HECTARE, 4, RoundingMode.HALF_UP);

                // 5. Busca e anexa os terrenos compatíveis
                List<TerrenoResponseDTO> terrenos = recomendacaoService.buscarTerrenosCompativeis(arvore, areaHectares);
                dto.setTerrenosCompativeis(terrenos);

            } catch (Exception e) {
                // Se a árvore ou terrenos não puderem ser buscados, loga o erro mas não quebra a requisição
                System.err.println("Falha ao hidratar terrenos compatíveis para o relatório " + entity.getId() + ": " + e.getMessage());
                dto.setTerrenosCompativeis(Collections.emptyList());
            }
        }

        return dto;
    }

    @Override
    public void deletarRelatorio(UUID relatorioId) {
        if (!relatorioRepository.existsById(relatorioId)) {
            throw new RecursoNaoEncontradoException("Relatório não encontrado: " + relatorioId);
        }
        relatorioRepository.deleteById(relatorioId);
    }

    @Override
    public RelatorioCalculadoDTO calcularEmissoesRelatorio(UUID relatorioId, Set<Integer> escopos) {
        RelatorioGHGEntity relatorio = relatorioRepository.findById(relatorioId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Relatório não encontrado: " + relatorioId));

        BigDecimal totalEmissoes = calculoService.calcularEmissoes(relatorio, escopos);
        relatorio.setEmissaoCalculadaCo2e(totalEmissoes);
        relatorio.setNivel(escopos.contains(3) ? NivelCompletude.COMPLETO : NivelCompletude.OPERACIONAL);

        // Limpa recomendações antigas se recalcular
        relatorio.setArvoreRecomendada(null);
        relatorio.setQuantidadeNecessaria(null);
        relatorio.setCustoTotalEstimado(null);

        RelatorioGHGEntity relatorioSalvo = relatorioRepository.save(relatorio);

        // Retorna o DTO de cálculo
        RelatorioCalculadoDTO dto = new RelatorioCalculadoDTO();
        dto.setId(relatorioSalvo.getId());
        dto.setAnoReferencia(relatorioSalvo.getAnoReferencia().getValue());
        dto.setEmissaoCalculadaCo2e(relatorioSalvo.getEmissaoCalculadaCo2e());
        dto.setNivel(relatorioSalvo.getNivel());
        return dto;
    }

    @Override
    @Transactional(readOnly = true) // Apenas leitura
    public List<RecomendacaoRanqueadaDTO> buscarRecomendacoes(UUID relatorioId) {
        RelatorioGHGEntity relatorio = relatorioRepository.findById(relatorioId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Relatório não encontrado: " + relatorioId));

        if (relatorio.getEmissaoCalculadaCo2e() == null || relatorio.getEmissaoCalculadaCo2e().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalStateException("As emissões devem ser calculadas (e ser maiores que zero) antes de buscar recomendações.");
        }

        return recomendacaoService.gerarRankingRecomendacoes(relatorio.getEmissaoCalculadaCo2e());
    }

    @Override
    public RelatorioGHGResponseDTO atribuirRecomendacao(UUID relatorioId, UUID arvoreId) {
        // 1. Busca o relatório
        RelatorioGHGEntity relatorio = relatorioRepository.findById(relatorioId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Relatório não encontrado: " + relatorioId));

        // 2. Valida se o cálculo já foi feito
        if (relatorio.getEmissaoCalculadaCo2e() == null || relatorio.getEmissaoCalculadaCo2e().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalStateException("As emissões devem ser calculadas antes de atribuir uma recomendação.");
        }

        // 3. Busca a árvore escolhida
        ArvoreResponseDTO arvore = arvoresServiceClient.buscarArvorePorId(arvoreId);

        // 4. Cálculo de Quantidade e Custo
        BigDecimal absorcaoTotalPorArvore = arvore.getTaxaAbsorcaoCo2Anual()
                .multiply(new BigDecimal(arvore.getTempoMaturidadeAnos()));

        if (absorcaoTotalPorArvore.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("A árvore selecionada não possui taxa de absorção válida.");
        }

        long quantidadeNecessaria = relatorio.getEmissaoCalculadaCo2e()
                .divide(absorcaoTotalPorArvore, 0, RoundingMode.CEILING).longValue();

        BigDecimal custoTotalEstimado = arvore.getCustoMedioMuda()
                .multiply(new BigDecimal(quantidadeNecessaria));

        // 5. Salva os dados da árvore no relatório
        relatorio.setArvoreIdRecomendada(arvore.getId());
        relatorio.setArvoreRecomendada(arvore.getNomePopular());
        relatorio.setQuantidadeNecessaria(quantidadeNecessaria);
        relatorio.setCustoTotalEstimado(custoTotalEstimado);

        // 6. Salva e retorna
        return relatorioMapper.toResponseDTO(relatorioRepository.save(relatorio));
    }
}