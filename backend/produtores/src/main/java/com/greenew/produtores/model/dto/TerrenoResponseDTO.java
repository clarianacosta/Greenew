package com.greenew.produtores.model.dto;

import com.greenew.produtores.config.dtos.BiomaResponseDTO;
import com.greenew.produtores.config.dtos.ClimaResponseDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TerrenoResponseDTO {
    private UUID id;
    private UUID produtorId;
    private BigDecimal latitude;
    private BigDecimal longitude;
    private BiomaResponseDTO biomaLocal; // Objeto Bioma
    private ClimaResponseDTO climaLocal; // Objeto Clima
//    private UUID biomaIdLocal;
//    private UUID climaIdLocal;
}