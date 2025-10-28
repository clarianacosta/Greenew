package com.greenew.produtores.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProdutorRequestDTO {

    @NotBlank(message = "O nome completo não pode ser vazio.")
    @Size(max = 255, message = "O nome completo deve ter no máximo 255 caracteres.")
    private String nomeCompleto;

    @NotBlank(message = "O e-mail não pode ser vazio.")
    @Email(message = "O formato do e-mail é inválido.")
    @Size(max = 255, message = "O e-mail deve ter no máximo 255 caracteres.")
    private String email;

    @Size(max = 20, message = "O celular deve ter no máximo 20 caracteres.")
    private String celular;
}