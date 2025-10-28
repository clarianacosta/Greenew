package com.greenew.produtores.service;

import com.greenew.produtores.model.dto.TerrenoRequestDTO;
import com.greenew.produtores.model.dto.TerrenoResponseDTO;
import com.greenew.produtores.model.entity.TerrenoEntity;

import java.util.List;
import java.util.UUID;

public interface TerrenoService {
    // CREATE
    TerrenoResponseDTO criar(TerrenoRequestDTO terrenoRequestDTO);

    // READ
    TerrenoResponseDTO buscarPorId(UUID id);
    List<TerrenoResponseDTO> buscarTodosPorProdutorId(UUID produtorId);
    List<TerrenoResponseDTO> buscarTodos();

    // UPDATE
    TerrenoResponseDTO atualizar(UUID id, TerrenoRequestDTO terrenoRequestDTO);

    // DELETE
    void deletar(UUID id);

    // Outros Métodos
    TerrenoResponseDTO mapEntityToFullResponseDTO(TerrenoEntity entity);
}