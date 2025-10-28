package com.greenew.relatorios.model.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.UUID;

@Data
public class FatorEmissaoResponseDTO {
    private UUID id;
    private String nomeAtividade;
    private String unidade;
    private BigDecimal fatorCo2;
    private BigDecimal fatorCh4;
    private BigDecimal fatorN2o;
    private String fonte;
}