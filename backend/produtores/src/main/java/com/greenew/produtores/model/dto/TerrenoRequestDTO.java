package com.greenew.produtores.model.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TerrenoRequestDTO {

    @NotNull(message = "A latitude é obrigatória.")
    private BigDecimal latitude;

    @NotNull(message = "A longitude é obrigatória.")
    private BigDecimal longitude;

    @NotNull(message = "O ID do bioma local é obrigatório.")
    private UUID biomaIdLocal;

    @NotNull(message = "O ID do clima local é obrigatório.")
    private UUID climaIdLocal;

    @NotNull(message = "O ID do produtor é obrigatório para a criação do terreno.")
    private UUID produtorId;
}