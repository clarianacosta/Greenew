package com.greenew.relatorios.service;

import com.greenew.relatorios.model.dto.AtividadeEmissoraRequestDTO;
import com.greenew.relatorios.model.dto.AtividadeEmissoraResponseDTO;
import java.util.List;
import java.util.UUID;

public interface AtividadeEmissoraService {
    AtividadeEmissoraResponseDTO adicionarAtividade(UUID relatorioId, AtividadeEmissoraRequestDTO requestDTO);
    List<AtividadeEmissoraResponseDTO> buscarAtividadesPorRelatorioId(UUID relatorioId);
    AtividadeEmissoraResponseDTO buscarAtividadePorId(UUID atividadeId);
    AtividadeEmissoraResponseDTO atualizarAtividade(UUID atividadeId, AtividadeEmissoraRequestDTO requestDTO);
    void deletarAtividade(UUID atividadeId);
}