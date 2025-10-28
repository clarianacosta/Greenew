package com.greenew.arvores.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BiomaResponseDTO {
    private UUID id;
    private String nome;
    private String descricao;
}