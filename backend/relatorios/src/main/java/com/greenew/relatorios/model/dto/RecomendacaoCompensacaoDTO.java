package com.greenew.relatorios.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RecomendacaoCompensacaoDTO {
    private String arvoreRecomendada;
    private long quantidadeNecessaria;
    private BigDecimal custoTotalEstimado;
    private BigDecimal totalEmissoesCO2eParaCompensar;
}