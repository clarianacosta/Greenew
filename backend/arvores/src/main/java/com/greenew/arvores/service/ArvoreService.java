package com.greenew.arvores.service;

import com.greenew.arvores.model.dto.ArvoreRequestDTO;
import com.greenew.arvores.model.dto.ArvoreResponseDTO;

import java.util.List;

public interface ArvoreService {
    // CREATE
    ArvoreResponseDTO criar(ArvoreRequestDTO arvoreRequestDTO);

    // READ
    ArvoreResponseDTO buscarPorId(Long id);
    List<ArvoreResponseDTO> buscarTodas();

    // UPDATE
    ArvoreResponseDTO atualizar(Long id, ArvoreRequestDTO arvoreRequestDTO);

    // DELETE
    void deletar(Long id);
}