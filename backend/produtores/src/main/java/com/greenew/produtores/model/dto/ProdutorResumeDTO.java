package com.greenew.produtores.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProdutorResumeDTO {
    private UUID id;
    private String nomeCompleto;
    private String email;
    private String celular;
}