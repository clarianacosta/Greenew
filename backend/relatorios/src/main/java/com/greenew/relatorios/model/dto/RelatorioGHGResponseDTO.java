package com.greenew.relatorios.model.dto;

import com.greenew.relatorios.model.entity.NivelCompletude;
import lombok.Data;
import java.math.BigDecimal;
import java.util.UUID;

@Data
public class RelatorioGHGResponseDTO {
    private UUID id;
    private int anoReferencia;
    private BigDecimal emissaoCalculadaCo2e;
    private NivelCompletude nivel;
    private String arvoreRecomendada;
    private Long quantidadeNecessaria;
    private BigDecimal custoTotalEstimado;
}