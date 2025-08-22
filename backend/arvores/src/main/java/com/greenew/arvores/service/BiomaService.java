package com.greenew.arvores.service;

import com.greenew.arvores.model.dto.BiomaDTO;
import com.greenew.arvores.model.entity.BiomaEntity;

import java.util.List;
import java.util.Optional;

public interface BiomaService {
    // CREATE
    BiomaDTO criar(BiomaDTO biomaDTO);

    // READ
    Optional<BiomaEntity> buscarPorId(Long id);
    List<BiomaDTO> buscarTodos();

    // UPDATE
    BiomaDTO atualizar(Long id, BiomaDTO biomaDTO);

    // DELETE
    void deletar(Long id);
}