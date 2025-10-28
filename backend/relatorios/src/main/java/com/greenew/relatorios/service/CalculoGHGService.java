package com.greenew.relatorios.service;

import com.greenew.relatorios.model.entity.RelatorioGHGEntity;
import java.math.BigDecimal;
import java.util.Set;

public interface CalculoGHGService {
    BigDecimal calcularEmissoes(RelatorioGHGEntity relatorio, Set<Integer> escopos);
}