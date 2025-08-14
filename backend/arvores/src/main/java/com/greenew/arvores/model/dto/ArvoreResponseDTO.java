package com.greenew.arvores.model.dto;


import lombok.Data;
import java.math.BigDecimal;
import java.util.Set;

@Data
public class ArvoreResponseDTO {
    private Long id;
    private String nomePopular;
    private String nomeCientifico;
    private BigDecimal taxaAbsorcaoCo2Anual;
    private BigDecimal custoMedioMuda;
    private Integer tempoMaturidadeAnos;
    private BigDecimal alturaMediaM;
    private BigDecimal diametroCopaMedioM;

    private Set<BiomaDTO> biomas;
    private Set<ClimaDTO> climas;
}