package com.greenew.relatorios.model.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class FatorEmissaoRequestDTO {
    @NotBlank
    private String nomeAtividade;

    @NotBlank
    private String unidade;

    @NotNull
    @Min(1) @Max(3)
    private Integer escopo;

    private Integer categoriaEscopo3;

    @NotNull @PositiveOrZero
    private BigDecimal fatorCo2;

    @NotNull @PositiveOrZero
    private BigDecimal fatorCh4;

    @NotNull @PositiveOrZero
    private BigDecimal fatorN2o;

    @NotBlank
    private String fonte;
}