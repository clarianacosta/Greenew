package com.greenew.arvores.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClimaRequestDTO {

    @NotBlank(message = "O nome do clima é obrigatório.")
    private String nome;

    @NotBlank(message = "A descrição do clima é obrigatória.")
    private String descricao;
}