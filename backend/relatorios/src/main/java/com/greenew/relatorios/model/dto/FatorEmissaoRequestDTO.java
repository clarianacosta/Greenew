package com.greenew.relatorios.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class FatorEmissaoRequestDTO {
    @NotBlank
    private String nomeAtividade;

    @NotBlank
    private String unidade;

    @NotNull @PositiveOrZero
    private BigDecimal fatorCo2;

    @NotNull @PositiveOrZero
    private BigDecimal fatorCh4;

    @NotNull @PositiveOrZero
    private BigDecimal fatorN2o;

    @NotBlank
    private String fonte;
}