package com.greenew.produtores.model.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Entity
@Table(name = "terrenos")
public class TerrenoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, precision = 9, scale = 6)
    private BigDecimal latitude;

    @Column(nullable = false, precision = 9, scale = 6)
    private BigDecimal longitude;

    @Column(name = "bioma_id_local")
    private UUID biomaIdLocal;

    @Column(name = "clima_id_local")
    private UUID climaIdLocal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "produtor_id", nullable = false)
    private ProdutorEntity produtor;
}