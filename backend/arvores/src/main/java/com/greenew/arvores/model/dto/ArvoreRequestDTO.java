package com.greenew.arvores.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArvoreRequestDTO {

    @NotBlank(message = "O nome popular não pode ser vazio.")
    @Size(max = 100, message = "O nome popular deve ter no máximo 100 caracteres.")
    private String nomePopular;

    @NotBlank(message = "O nome científico não pode ser vazio.")
    @Size(max = 150, message = "O nome científico deve ter no máximo 150 caracteres.")
    private String nomeCientifico;

    @NotNull(message = "A taxa de absorção de CO2 é obrigatória.")
    @Positive(message = "A taxa de absorção de CO2 deve ser um valor positivo.")
    private BigDecimal taxaAbsorcaoCo2Anual;

    @Positive(message = "O custo da muda deve ser um valor positivo.")
    private BigDecimal custoMedioMuda;

    @Positive(message = "O tempo de maturidade deve ser um valor positivo.")
    private Integer tempoMaturidadeAnos;

    @Positive(message = "A altura média deve ser um valor positivo.")
    private BigDecimal alturaMediaM;

    @Positive(message = "O diâmetro da copa deve ser um valor positivo.")
    private BigDecimal diametroCopaMedioM;

    @NotEmpty(message = "A árvore deve estar associada a pelo menos um bioma.")
    private Set<UUID> biomasIds;

    @NotEmpty(message = "A árvore deve estar associada a pelo menos um clima.")
    private Set<UUID> climasIds;
}