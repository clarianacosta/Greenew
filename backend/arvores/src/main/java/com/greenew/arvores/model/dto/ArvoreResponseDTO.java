package com.greenew.arvores.model.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArvoreResponseDTO {
    private UUID id;
    private String nomePopular;
    private String nomeCientifico;
    private BigDecimal taxaAbsorcaoCo2Anual;
    private BigDecimal custoMedioMuda;
    private Integer tempoMaturidadeAnos;
    private BigDecimal alturaMediaM;
    private BigDecimal diametroCopaMedioM;

    private Set<BiomaResponseDTO> biomas;
    private Set<ClimaResponseDTO> climas;
}