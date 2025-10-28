package com.greenew.arvores.service;

import com.greenew.arvores.model.dto.ClimaRequestDTO; // NOVO: Para entrada
import com.greenew.arvores.model.dto.ClimaResponseDTO;
import com.greenew.arvores.model.entity.ClimaEntity; // Importar a Entity

import java.util.List;
import java.util.UUID;

public interface ClimaService {
    // CREATE: Aceita RequestDTO
    ClimaResponseDTO criar(ClimaRequestDTO climaRequestDTO);

    // READ: Retorna ResponseDTO
    ClimaResponseDTO buscarPorId(UUID id);
    List<ClimaResponseDTO> buscarTodos();

    ClimaEntity getEntityById(UUID id);

    // UPDATE: Aceita RequestDTO
    ClimaResponseDTO atualizar(UUID id, ClimaRequestDTO climaRequestDTO);

    // DELETE
    void deletar(UUID id);
}