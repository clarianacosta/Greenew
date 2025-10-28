package com.greenew.empresas.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.UUID;

/**
 * DTO para representar os dados de uma Empresa enviados como resposta.
 * Versão simplificada para o contexto do microserviço focado apenas em empresas.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmpresaResponseDTO {

    private UUID id;

    private String razaoSocial;

    private String cnpj;
}