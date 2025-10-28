package com.greenew.relatorios.model.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.UUID;

@Data
public class RelatorioGHGRequestDTO {
    @NotNull(message = "O ID da empresa é obrigatório.")
    private UUID empresaId;

    @NotNull(message = "O ano de referência é obrigatório.")
    @Min(value = 1990, message = "O ano de referência deve ser de 1990 ou posterior.")
    private Integer anoReferencia;
}