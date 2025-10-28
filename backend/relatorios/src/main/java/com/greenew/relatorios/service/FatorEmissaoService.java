package com.greenew.relatorios.service;

import com.greenew.relatorios.model.dto.FatorEmissaoRequestDTO;
import com.greenew.relatorios.model.dto.FatorEmissaoResponseDTO;
import java.util.List;
import java.util.UUID;

public interface FatorEmissaoService {
    FatorEmissaoResponseDTO criarFator(FatorEmissaoRequestDTO dto);
    FatorEmissaoResponseDTO buscarPorId(UUID id);
    List<FatorEmissaoResponseDTO> buscarTodos();
    FatorEmissaoResponseDTO atualizarFator(UUID id, FatorEmissaoRequestDTO dto);
    void deletarFator(UUID id);
}