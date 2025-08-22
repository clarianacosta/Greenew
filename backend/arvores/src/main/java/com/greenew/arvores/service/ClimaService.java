package com.greenew.arvores.service;

import com.greenew.arvores.model.dto.ClimaDTO;
import com.greenew.arvores.model.entity.ClimaEntity;

import java.util.List;
import java.util.Optional;

public interface ClimaService {
    // CREATE
    ClimaDTO criar(ClimaDTO climaDTO);

    // READ
    Optional<ClimaEntity> buscarPorId(Long id);
    List<ClimaDTO> buscarTodos();

    // UPDATE
    ClimaDTO atualizar(Long id, ClimaDTO climaDTO);

    // DELETE
    void deletar(Long id);
}