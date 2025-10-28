package com.greenew.relatorios.config.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmpresaResponseDTO {

    private UUID id;

    private String razaoSocial;

    private String cnpj;
}