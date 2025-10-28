package com.greenew.arvores.service;

import com.greenew.arvores.model.dto.ArvoreRequestDTO;
import com.greenew.arvores.model.dto.ArvoreResponseDTO;

import java.util.List;
import java.util.UUID;
import java.util.UUID;

public interface ArvoreService {
    // CREATE
    ArvoreResponseDTO criar(ArvoreRequestDTO arvoreRequestDTO);

    // READ
    ArvoreResponseDTO buscarPorId(UUID id);
    List<ArvoreResponseDTO> buscarTodas();

    // UPDATE
    ArvoreResponseDTO atualizar(UUID id, ArvoreRequestDTO arvoreRequestDTO);

    // DELETE
    void deletar(UUID id);
}