package com.greenew.produtores.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProdutorResponseDTO {
    private UUID id;
    private String nomeCompleto;
    private String email;
    private String celular;
    private Set<TerrenoResponseDTO> terrenos;
}