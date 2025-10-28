package com.greenew.empresas.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para receber dados na criação ou atualização de uma Empresa.
 * Esta classe permanece a mesma após a refatoração do serviço.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmpresaRequestDTO {

    @NotBlank(message = "A razão social é obrigatória.")
    @Size(max = 255, message = "A razão social deve ter no máximo 255 caracteres.")
    private String razaoSocial;

    @NotBlank(message = "O CNPJ é obrigatório.")
    @Pattern(regexp = "\\d{14}", message = "O CNPJ deve conter exatamente 14 dígitos numéricos.")
    private String cnpj;
}