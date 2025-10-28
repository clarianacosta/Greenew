package com.greenew.arvores.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class ArvoresBiomasId implements Serializable {

    @Column(name = "arvore_id")
    private UUID arvoreId;

    @Column(name = "bioma_id")
    private UUID biomaId;
}