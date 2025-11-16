package com.greenew.relatorios.config.dto;

import lombok.Data;
import java.util.UUID;

@Data
public class ProdutorResumeDTO {
    private UUID id;
    private String nomeCompleto;
    private String email;
    private String celular;
}