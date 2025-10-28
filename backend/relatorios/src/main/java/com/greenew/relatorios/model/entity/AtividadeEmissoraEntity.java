package com.greenew.relatorios.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;
import java.util.UUID;

/**
 * Entidade que representa uma única atividade geradora de emissões
 * dentro de um Relatório GHG.
 */
@Entity
@Table(name = "atividades_emissoras")
@Getter
@Setter
public class AtividadeEmissoraEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private String nome; // Ex: "Diesel Rodoviário", "Eletricidade da Rede"

    @Column(nullable = false)
    private int escopo; // 1, 2 ou 3

    @Column(name = "categoria_escopo3", nullable = true)
    private Integer categoriaEscopo3; // Categoria do Escopo 3 (1 a 15), se aplicável

    @Column(nullable = false)
    private String unidade; // Ex: "Litro", "kWh", "tonelada"

    @Column(nullable = false, precision = 15, scale = 4)
    private BigDecimal quantidade; // O "Dado de Atividade", ex: 5000.00

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "relatorio_id", nullable = false)
    private RelatorioGHGEntity relatorio;
}