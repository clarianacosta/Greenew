package com.greenew.relatorios.service.impl;

import com.greenew.relatorios.config.client.EmpresasServiceClient;
import com.greenew.relatorios.exception.RecursoNaoEncontradoException;
import com.greenew.relatorios.model.dto.*;
import com.greenew.relatorios.model.entity.NivelCompletude;
import com.greenew.relatorios.model.entity.RelatorioGHGEntity;
import com.greenew.relatorios.model.mapper.RelatorioGHGMapper;
import com.greenew.relatorios.repository.RelatorioGHGRepository;
import com.greenew.relatorios.service.CalculoGHGService;
import com.greenew.relatorios.service.RecomendacaoService;
import com.greenew.relatorios.service.RelatorioGHGService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class RelatorioGHGServiceImpl implements RelatorioGHGService {

    private final RelatorioGHGRepository relatorioRepository;
    private final RelatorioGHGMapper relatorioMapper;
    private final CalculoGHGService calculoService;
    private final RecomendacaoService recomendacaoService;
    private final EmpresasServiceClient empresasServiceClient;

    public RelatorioGHGServiceImpl(RelatorioGHGRepository rRepo, RelatorioGHGMapper rMapper, CalculoGHGService cService, RecomendacaoService recService, EmpresasServiceClient eClient) {
        this.relatorioRepository = rRepo;
        this.relatorioMapper = rMapper;
        this.calculoService = cService;
        this.recomendacaoService = recService;
        this.empresasServiceClient = eClient;
    }

    @Override
    public RelatorioGHGResponseDTO criarRelatorio(RelatorioGHGRequestDTO requestDTO) {
        if (!empresasServiceClient.verificarSeEmpresaExiste(requestDTO.getEmpresaId())) {
            throw new RecursoNaoEncontradoException("Impossível criar relatório: Empresa não encontrada com ID " + requestDTO.getEmpresaId());
        }
        RelatorioGHGEntity entity = relatorioMapper.toEntity(requestDTO);
        return relatorioMapper.toResponseDTO(relatorioRepository.save(entity));
    }

//    @Override
//    public RelatorioGHGResponseDTO finalizarRelatorio(UUID relatorioId, UUID terrenoId, Set<Integer> escopos) {
//        RelatorioGHGEntity relatorio = relatorioRepository.findById(relatorioId)
//                .orElseThrow(() -> new RecursoNaoEncontradoException("Relatório não encontrado: " + relatorioId));
//
//        BigDecimal totalEmissoes = calculoService.calcularEmissoes(relatorio, escopos);
//        relatorio.setEmissaoCalculadaCo2e(totalEmissoes);
//        relatorio.setNivel(escopos.contains(3) ? NivelCompletude.COMPLETO : NivelCompletude.OPERACIONAL);
//
//        if (totalEmissoes.compareTo(BigDecimal.ZERO) > 0) {
//            RecomendacaoCompensacaoDTO recomendacao = recomendacaoService.gerarRecomendacao(totalEmissoes, terrenoId);
//            relatorio.setArvoreRecomendada(recomendacao.getArvoreRecomendada());
//            relatorio.setQuantidadeNecessaria(recomendacao.getQuantidadeNecessaria());
//            relatorio.setCustoTotalEstimado(recomendacao.getCustoTotalEstimado());
//        }
//
//        return relatorioMapper.toResponseDTO(relatorioRepository.save(relatorio));
//    }

    @Override
    @Transactional(readOnly = true)
    public RelatorioGHGResponseDTO buscarPorId(UUID relatorioId) {
        return relatorioRepository.findById(relatorioId)
                .map(relatorioMapper::toResponseDTO)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Relatório não encontrado: " + relatorioId));
    }

    @Override
    @Transactional(readOnly = true)
    public List<RelatorioGHGResponseDTO> buscarTodosPorEmpresaId(UUID empresaId) {
        // 1. Usa o repositório para buscar todas as entidades de relatório para a empresa
        List<RelatorioGHGEntity> relatoriosDaEmpresa = relatorioRepository.findAllByEmpresaId(empresaId);

        // 2. Converte a lista de entidades para uma lista de DTOs
        return relatoriosDaEmpresa.stream()
                .map(relatorioMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deletarRelatorio(UUID relatorioId) {
        if (!relatorioRepository.existsById(relatorioId)) {
            throw new RecursoNaoEncontradoException("Relatório não encontrado: " + relatorioId);
        }
        relatorioRepository.deleteById(relatorioId);
    }

    @Override
    public RelatorioCalculadoDTO calcularEmissoesRelatorio(UUID relatorioId, Set<Integer> escopos) {
        RelatorioGHGEntity relatorio = relatorioRepository.findById(relatorioId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Relatório não encontrado: " + relatorioId));

        BigDecimal totalEmissoes = calculoService.calcularEmissoes(relatorio, escopos);
        relatorio.setEmissaoCalculadaCo2e(totalEmissoes);
        relatorio.setNivel(escopos.contains(3) ? NivelCompletude.COMPLETO : NivelCompletude.OPERACIONAL);

        // Limpa recomendações antigas se recalcular
        relatorio.setArvoreRecomendada(null);
        relatorio.setQuantidadeNecessaria(null);
        relatorio.setCustoTotalEstimado(null);

        RelatorioGHGEntity relatorioSalvo = relatorioRepository.save(relatorio);

        // Retorna o DTO de cálculo
        RelatorioCalculadoDTO dto = new RelatorioCalculadoDTO();
        dto.setId(relatorioSalvo.getId());
        dto.setAnoReferencia(relatorioSalvo.getAnoReferencia().getValue());
        dto.setEmissaoCalculadaCo2e(relatorioSalvo.getEmissaoCalculadaCo2e());
        dto.setNivel(relatorioSalvo.getNivel());
        return dto;
    }

    @Override
    @Transactional(readOnly = true) // Apenas leitura
    public List<RecomendacaoRanqueadaDTO> buscarRecomendacoes(UUID relatorioId) {
        RelatorioGHGEntity relatorio = relatorioRepository.findById(relatorioId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Relatório não encontrado: " + relatorioId));

        if (relatorio.getEmissaoCalculadaCo2e() == null || relatorio.getEmissaoCalculadaCo2e().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalStateException("As emissões devem ser calculadas (e ser maiores que zero) antes de buscar recomendações.");
        }

        return recomendacaoService.gerarRankingRecomendacoes(relatorio.getEmissaoCalculadaCo2e());
    }

    @Override
    public RelatorioGHGResponseDTO atribuirRecomendacao(UUID relatorioId, UUID terrenoId, String nomeArvore) {
        RelatorioGHGEntity relatorio = relatorioRepository.findById(relatorioId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Relatório não encontrado: " + relatorioId));

        if (relatorio.getEmissaoCalculadaCo2e() == null || relatorio.getEmissaoCalculadaCo2e().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalStateException("As emissões devem ser calculadas antes de atribuir uma recomendação.");
        }

        // Recalcula a recomendação específica escolhida para garantir os dados
        // (Esta é uma simplificação; o ideal seria receber o DTO da recomendação escolhida)

        List<RecomendacaoRanqueadaDTO> ranking = recomendacaoService.gerarRankingRecomendacoes(relatorio.getEmissaoCalculadaCo2e());

        RecomendacaoRanqueadaDTO recomendacaoEscolhida = ranking.stream()
                .filter(r -> r.getArvore().getNomePopular().equals(nomeArvore))
                .findFirst()
                .orElseThrow(() -> new RecursoNaoEncontradoException("Recomendação com a árvore " + nomeArvore + " não é válida para este relatório."));

        // Valida se o terreno escolhido está na lista de compatíveis
        boolean terrenoValido = recomendacaoEscolhida.getTerrenosCompatíveis().stream()
                .anyMatch(t -> t.getId().equals(terrenoId));

        if (!terrenoValido) {
            throw new IllegalArgumentException("O terreno selecionado não é compatível com a árvore e área necessárias.");
        }

        // Salva a recomendação no relatório
        relatorio.setArvoreRecomendada(recomendacaoEscolhida.getArvore().getNomePopular());
        relatorio.setQuantidadeNecessaria(recomendacaoEscolhida.getQuantidadeNecessaria());
        relatorio.setCustoTotalEstimado(recomendacaoEscolhida.getCustoTotalEstimado());

        return relatorioMapper.toResponseDTO(relatorioRepository.save(relatorio));
    }
}