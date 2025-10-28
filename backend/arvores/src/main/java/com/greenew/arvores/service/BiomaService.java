package com.greenew.arvores.service;

import com.greenew.arvores.model.dto.BiomaRequestDTO; // NOVO: Para entrada
import com.greenew.arvores.model.dto.BiomaResponseDTO;
import com.greenew.arvores.model.entity.BiomaEntity; // Importar a Entity para uso interno

import java.util.List;
import java.util.UUID;

public interface BiomaService {
    // CREATE: Aceita RequestDTO
    BiomaResponseDTO criar(BiomaRequestDTO biomaRequestDTO);

    // READ: Retorna ResponseDTO
    BiomaResponseDTO buscarPorId(UUID id);
    List<BiomaResponseDTO> buscarTodos();

    BiomaEntity getEntityById(UUID id);

    // UPDATE: Aceita RequestDTO
    BiomaResponseDTO atualizar(UUID id, BiomaRequestDTO biomaRequestDTO);

    // DELETE
    void deletar(UUID id);
}