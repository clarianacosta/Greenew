package com.greenew.relatorios.model.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;

@Data
public class AtividadeEmissoraRequestDTO {

    @NotBlank(message = "O nome da atividade é obrigatório (ex: Diesel Rodoviário).")
    private String nome;

    @NotNull(message = "O escopo é obrigatório.")
    @Min(value = 1, message = "O escopo deve ser 1, 2 ou 3.")
    @Max(value = 3, message = "O escopo deve ser 1, 2 ou 3.")
    private Integer escopo;

    // Opcional, apenas se escopo for 3
    private Integer categoriaEscopo3;

    @NotBlank(message = "A unidade de medida é obrigatória (ex: Litro, kWh).")
    private String unidade;

    @NotNull(message = "A quantidade é obrigatória.")
    @Positive(message = "A quantidade deve ser um valor positivo.")
    private BigDecimal quantidade;
}