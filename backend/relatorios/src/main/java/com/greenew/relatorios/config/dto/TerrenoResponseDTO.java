package com.greenew.relatorios.config.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.UUID;

@Data
public class TerrenoResponseDTO {
    private UUID id;
    // private UUID produtorId;
    private ProdutorResumeDTO produtor;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private BigDecimal areaDisponivelHectares;
    private BiomaResponseDTO biomaLocal;
    private ClimaResponseDTO climaLocal;
}