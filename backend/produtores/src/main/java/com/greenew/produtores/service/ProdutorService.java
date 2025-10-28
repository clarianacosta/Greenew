package com.greenew.produtores.service;

import com.greenew.produtores.model.dto.ProdutorRequestDTO;
import com.greenew.produtores.model.dto.ProdutorResponseDTO;

import java.util.List;
import java.util.UUID;

public interface ProdutorService {
    // CREATE
    ProdutorResponseDTO criar(ProdutorRequestDTO produtorRequestDTO);

    // READ
    ProdutorResponseDTO buscarPorId(UUID id);
    List<ProdutorResponseDTO> buscarTodos();

    // UPDATE
    ProdutorResponseDTO atualizar(UUID id, ProdutorRequestDTO produtorRequestDTO);

    // DELETE
    void deletar(UUID id);
}