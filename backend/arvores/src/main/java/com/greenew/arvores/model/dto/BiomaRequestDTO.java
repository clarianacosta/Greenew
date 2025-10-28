package com.greenew.arvores.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BiomaRequestDTO {

    @NotBlank(message = "O nome do bioma é obrigatório.")
    private String nome;

    @NotBlank(message = "A descrição do bioma é obrigatória.")
    private String descricao;
}