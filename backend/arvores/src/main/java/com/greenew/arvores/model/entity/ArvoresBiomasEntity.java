package com.greenew.arvores.model.entity;

import jakarta.persistence.Entity;
import lombok.Data;

import jakarta.persistence.*;

@Data
@Entity
@Table(name = "arvores_biomas")
public class ArvoresBiomasEntity {
    @EmbeddedId
    private ArvoresBiomasId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("arvoreId")
    @JoinColumn(name = "arvore_id")
    private ArvoreEntity arvore;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("biomaId")
    @JoinColumn(name = "bioma_id")
    private BiomaEntity bioma;
}
