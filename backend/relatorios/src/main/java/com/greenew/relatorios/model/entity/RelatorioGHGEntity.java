package com.greenew.relatorios.model.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import java.math.BigDecimal;
import java.time.Year;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Entidade principal do serviço, representa um inventário de emissões de GEE
 * para uma empresa em um determinado ano.
 */
@Data
@Entity
@Table(name = "relatorios_ghg")
public class RelatorioGHGEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    // Chave estrangeira "lógica" para o empresas-service.
    @Column(name = "empresa_id", nullable = false)
    private UUID empresaId;

    @Column(name = "ano_referencia", nullable = false)
    private Year anoReferencia;

    @Column(name = "emissao_calculada_co2e", nullable = false, precision = 15, scale = 4)
    private BigDecimal emissaoCalculadaCo2e;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private NivelCompletude nivel;

    // Campos para armazenar a recomendação gerada
    @Column(name = "arvore_recomendada", nullable = true)
    private String arvoreRecomendada;

    @Column(name = "quantidade_necessaria", nullable = true)
    private Long quantidadeNecessaria;

    @Column(name = "custo_total_estimado", precision = 15, scale = 2, nullable = true)
    private BigDecimal custoTotalEstimado;

    // Relacionamento com as atividades que compõem este relatório.
    @OneToMany(mappedBy = "relatorio", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<AtividadeEmissoraEntity> atividades = new HashSet<>();
}