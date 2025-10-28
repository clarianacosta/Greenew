package com.greenew.arvores.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class ArvoresClimasId implements Serializable {
    @Column(name = "arvore_id")
    private UUID arvoreId;

    @Column(name = "clima_id")
    private UUID climaId;
}
