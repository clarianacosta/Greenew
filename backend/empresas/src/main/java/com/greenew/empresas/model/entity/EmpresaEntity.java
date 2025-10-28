package com.greenew.empresas.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.util.UUID;

/**
 * Entidade JPA que representa uma Empresa no banco de dados.
 * No contexto deste microserviço, sua única responsabilidade é
 * armazenar os dados cadastrais da empresa.
 */
@Data
@Entity
@Table(name = "empresas")
public class EmpresaEntity {

    /**
     * Identificador único da empresa, gerado automaticamente.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    /**
     * Nome oficial da empresa.
     */
    @Column(name = "razao_social", nullable = false, length = 255)
    private String razaoSocial;

    /**
     * CNPJ da empresa, contendo apenas os 14 dígitos.
     * É único para garantir que não haja empresas duplicadas.
     */
    @Column(nullable = false, unique = true, length = 14)
    private String cnpj;
}