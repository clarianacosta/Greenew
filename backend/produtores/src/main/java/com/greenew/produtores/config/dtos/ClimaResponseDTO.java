package com.greenew.produtores.config.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClimaResponseDTO {
    private UUID id;
    private String nome;
    private String descricao;
}