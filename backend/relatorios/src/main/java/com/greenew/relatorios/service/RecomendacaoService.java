package com.greenew.relatorios.service;

import com.greenew.relatorios.model.dto.RecomendacaoCompensacaoDTO;
import java.math.BigDecimal;
import java.util.UUID;

public interface RecomendacaoService {
    RecomendacaoCompensacaoDTO gerarRecomendacao(BigDecimal totalEmissoesCO2e, UUID terrenoId);
}