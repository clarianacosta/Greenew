package com.greenew.relatorios.service;

import com.greenew.relatorios.model.dto.RecomendacaoCompensacaoDTO;
import com.greenew.relatorios.model.dto.RecomendacaoRanqueadaDTO;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface RecomendacaoService {
    // RecomendacaoCompensacaoDTO gerarRecomendacao(BigDecimal totalEmissoesCO2e, UUID terrenoId);
    List<RecomendacaoRanqueadaDTO> gerarRankingRecomendacoes(BigDecimal totalEmissoesCO2e);
}