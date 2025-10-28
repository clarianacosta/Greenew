package com.greenew.relatorios.config.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.util.UUID;

@Data
public class TerrenoResponseDTO {
    private UUID id;
    private UUID produtorId;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private BiomaResponseDTO biomaLocal;
    private ClimaResponseDTO climaLocal;
}