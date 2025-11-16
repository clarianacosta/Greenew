package com.greenew.relatorios.service;

import com.greenew.relatorios.model.dto.RecomendacaoRanqueadaDTO;
import com.greenew.relatorios.model.dto.RelatorioCalculadoDTO;
import com.greenew.relatorios.model.dto.RelatorioGHGRequestDTO;
import com.greenew.relatorios.model.dto.RelatorioGHGResponseDTO;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface RelatorioGHGService {
    RelatorioGHGResponseDTO criarRelatorio(RelatorioGHGRequestDTO requestDTO);
    RelatorioGHGResponseDTO buscarPorId(UUID relatorioId);
    List<RelatorioGHGResponseDTO> buscarTodosPorEmpresaId(UUID empresaId);
    void deletarRelatorio(UUID relatorioId);
    //RelatorioGHGResponseDTO finalizarRelatorio(UUID relatorioId, UUID terrenoId, Set<Integer> escopos);
    RelatorioCalculadoDTO calcularEmissoesRelatorio(UUID relatorioId, Set<Integer> escopos);
    List<RecomendacaoRanqueadaDTO> buscarRecomendacoes(UUID relatorioId);
    RelatorioGHGResponseDTO atribuirRecomendacao(UUID relatorioId, UUID terrenoId, String nomeArvore);
}