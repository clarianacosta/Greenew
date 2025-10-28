package com.greenew.arvores.model.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "arvores_climas")
public class ArvoresClimasEntity {
    @EmbeddedId
    private ArvoresClimasId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("arvoreId")
    @JoinColumn(name = "arvore_id")
    private ArvoreEntity arvore;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("climaId")
    @JoinColumn(name = "clima_id")
    private ClimaEntity clima;
}
