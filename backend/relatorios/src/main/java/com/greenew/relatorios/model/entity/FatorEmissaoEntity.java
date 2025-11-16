package com.greenew.relatorios.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.util.UUID;

/**
 * Entidade que armazena os fatores de emissão oficiais do GHG Protocol.
 * Cada registro contém os coeficientes para converter um dado de atividade
 * em emissões de CO₂, CH₄ e N₂O.
 */
@Entity
@Table(name = "fatores_emissao", uniqueConstraints = {
        // Garante que não pode haver dois fatores com o mesmo nome e unidade.
        @UniqueConstraint(columnNames = {"nome_atividade", "unidade"})
})
@Getter
@Setter
public class FatorEmissaoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(name = "nome_atividade", nullable = false)
    private String nomeAtividade;

    @Column(nullable = false)
    private String unidade;

    @Column(nullable = false)
    private Integer escopo;

    @Column(name = "categoria_escopo3", nullable = true)
    private Integer categoriaEscopo3;

    @Column(name = "fator_co2", nullable = false, precision = 15, scale = 8)
    private BigDecimal fatorCo2; // Emissão de CO₂ em kg por unidade

    @Column(name = "fator_ch4", nullable = false, precision = 15, scale = 8)
    private BigDecimal fatorCh4; // Emissão de CH₄ em kg por unidade

    @Column(name = "fator_n2o", nullable = false, precision = 15, scale = 8)
    private BigDecimal fatorN2o; // Emissão de N₂O em kg por unidade

    @Column(nullable = false)
    private String fonte; // Fonte do dado, ex: "GHG Protocol Brasil 2024"
}