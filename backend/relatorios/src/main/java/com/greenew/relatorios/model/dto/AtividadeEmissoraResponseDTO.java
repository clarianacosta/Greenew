package com.greenew.relatorios.model.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.UUID;

@Data
public class AtividadeEmissoraResponseDTO {
    private UUID id;
    private String nome;
    private int escopo;
    private Integer categoriaEscopo3;
    private String unidade;
    private BigDecimal quantidade;
}