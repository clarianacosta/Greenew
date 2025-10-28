package com.greenew.relatorios.config.dto;

import lombok.Data;
import java.util.UUID;

@Data
public class BiomaResponseDTO {
    private UUID id;
    private String nome;
}