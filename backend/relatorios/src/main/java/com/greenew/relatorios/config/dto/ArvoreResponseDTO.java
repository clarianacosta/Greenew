package com.greenew.relatorios.config.dto;


import lombok.Data;
import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

@Data
public class ArvoreResponseDTO {
    private UUID id;
    private String nomePopular;
    private BigDecimal taxaAbsorcaoCo2Anual;
    private BigDecimal custoMedioMuda;
    private Integer tempoMaturidadeAnos;
    private BigDecimal alturaMediaM;
    private BigDecimal diametroCopaMedioM;
    private Set<BiomaResponseDTO> biomas;
    private Set<ClimaResponseDTO> climas;
}