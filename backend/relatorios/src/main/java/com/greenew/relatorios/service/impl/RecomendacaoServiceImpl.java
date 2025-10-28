package com.greenew.relatorios.service.impl;

import com.greenew.relatorios.config.client.ArvoresServiceClient;
import com.greenew.relatorios.config.client.ProdutoresServiceClient;
import com.greenew.relatorios.config.dto.ArvoreResponseDTO;
import com.greenew.relatorios.config.dto.TerrenoResponseDTO;
import com.greenew.relatorios.exception.RecursoNaoEncontradoException;
import com.greenew.relatorios.model.dto.RecomendacaoCompensacaoDTO;
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

    public RecomendacaoServiceImpl(ArvoresServiceClient aClient, ProdutoresServiceClient pClient) {
        this.arvoresServiceClient = aClient;
        this.produtoresServiceClient = pClient;
    }

    @Override
    public RecomendacaoCompensacaoDTO gerarRecomendacao(BigDecimal totalEmissoesCO2e, UUID terrenoId) {
        TerrenoResponseDTO terreno = produtoresServiceClient.buscarTerrenoPorId(terrenoId);
        List<ArvoreResponseDTO> todasAsArvores = arvoresServiceClient.buscarTodasArvores();

        List<ArvoreResponseDTO> arvoresCompatíveis = todasAsArvores.stream()
                .filter(arvore -> arvore.getBiomas().stream().anyMatch(b -> b.getId().equals(terreno.getBiomaLocal().getId())))
                .filter(arvore -> arvore.getClimas().stream().anyMatch(c -> c.getId().equals(terreno.getClimaLocal().getId())))
                .collect(Collectors.toList());

        if (arvoresCompatíveis.isEmpty()) {
            throw new RecursoNaoEncontradoException("Nenhuma árvore compatível encontrada para o bioma e clima do terreno selecionado.");
        }

        ArvoreResponseDTO melhorArvore = Collections.max(arvoresCompatíveis, Comparator.comparing(this::calcularPontuacaoFinal));
        BigDecimal absorcaoTotalPorArvore = melhorArvore.getTaxaAbsorcaoCo2Anual().multiply(new BigDecimal(melhorArvore.getTempoMaturidadeAnos()));
        long quantidadeNecessaria = totalEmissoesCO2e.divide(absorcaoTotalPorArvore, 0, RoundingMode.CEILING).longValue();
        BigDecimal custoTotal = melhorArvore.getCustoMedioMuda().multiply(new BigDecimal(quantidadeNecessaria));

        return new RecomendacaoCompensacaoDTO(melhorArvore.getNomePopular(), quantidadeNecessaria, custoTotal, totalEmissoesCO2e);
    }

    private double calcularPontuacaoFinal(ArvoreResponseDTO arvore) {
        double pesoAbsorcao = 0.40, pesoCusto = 0.30, pesoTempo = 0.20, pesoEspaco = 0.10;
        double scoreAbsorcao = arvore.getTaxaAbsorcaoCo2Anual().doubleValue();
        double scoreCusto = 1.0 / arvore.getCustoMedioMuda().doubleValue();
        double scoreTempo = 1.0 / arvore.getTempoMaturidadeAnos();
        double raioCopa = arvore.getDiametroCopaMedioM().doubleValue() / 2.0;
        double scoreEspaco = 1.0 / (Math.PI * raioCopa * raioCopa);
        return (scoreAbsorcao * pesoAbsorcao) + (scoreCusto * pesoCusto) + (scoreTempo * pesoTempo) + (scoreEspaco * pesoEspaco);
    }
}