package com.greenew.relatorios.service;

import com.greenew.relatorios.config.dto.ArvoreResponseDTO;
import com.greenew.relatorios.config.dto.TerrenoResponseDTO;
import com.greenew.relatorios.model.dto.RecomendacaoCompensacaoDTO;
import com.greenew.relatorios.model.dto.RecomendacaoRanqueadaDTO;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface RecomendacaoService {
    List<RecomendacaoRanqueadaDTO> gerarRankingRecomendacoes(BigDecimal totalEmissoesCO2e);
    BigDecimal METROS_QUADRADOS_POR_HECTARE = new BigDecimal("10000");
    double calcularAreaPorArvore(ArvoreResponseDTO arvore);
    List<TerrenoResponseDTO> buscarTerrenosCompativeis(ArvoreResponseDTO arvore, BigDecimal areaNecessariaHectares);
}