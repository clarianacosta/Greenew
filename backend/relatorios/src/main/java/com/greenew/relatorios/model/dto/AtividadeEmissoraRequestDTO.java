package com.greenew.relatorios.model.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import java.math.BigDecimal;
import java.util.UUID;

@Data
public class AtividadeEmissoraRequestDTO {

    @NotNull(message = "O ID do fator de emissão é obrigatório.")
    private UUID fatorEmissaoId;

    @NotNull(message = "A quantidade é obrigatória.")
    @Positive(message = "A quantidade deve ser um valor positivo.")
    private BigDecimal quantidade;
}