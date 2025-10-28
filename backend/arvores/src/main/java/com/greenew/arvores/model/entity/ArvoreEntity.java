package com.greenew.arvores.model.entity;


import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@Entity
@Table(name = "arvores")
public class ArvoreEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "nome_popular", nullable = false, length = 100)
    private String nomePopular;

    @Column(name = "nome_cientifico", unique = true, nullable = false, length = 150)
    private String nomeCientifico;

    @Column(name = "taxa_absorcao_co2_anual", nullable = false, precision = 10, scale = 4)
    private BigDecimal taxaAbsorcaoCo2Anual;

    // --- COLUNAS ADICIONADAS ---
    @Column(name = "custo_medio_muda", precision = 8, scale = 2)
    private BigDecimal custoMedioMuda;

    @Column(name = "tempo_maturidade_anos")
    private Integer tempoMaturidadeAnos;

    @Column(name = "altura_media_m", precision = 5, scale = 2)
    private BigDecimal alturaMediaM;

    @Column(name = "diametro_copa_medio_m", precision = 5, scale = 2)
    private BigDecimal diametroCopaMedioM;

    // Relacionamento 1:n com a entidade de junção ArvoresBiomasEntity
    @OneToMany(mappedBy = "arvore", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<ArvoresBiomasEntity> biomasAssociados = new HashSet<>();

    // Relacionamento Um-para-Muitos com a entidade de junção ArvoresClimas
    @OneToMany(mappedBy = "arvore", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<ArvoresClimasEntity> climasAssociados = new HashSet<>();
}