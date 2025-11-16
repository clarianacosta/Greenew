package com.greenew.relatorios.service.impl;

import com.greenew.relatorios.exception.RecursoNaoEncontradoException;
import com.greenew.relatorios.model.dto.AtividadeEmissoraRequestDTO;
import com.greenew.relatorios.model.dto.AtividadeEmissoraResponseDTO;
import com.greenew.relatorios.model.entity.AtividadeEmissoraEntity;
import com.greenew.relatorios.model.entity.FatorEmissaoEntity;
import com.greenew.relatorios.model.entity.RelatorioGHGEntity;
import com.greenew.relatorios.model.mapper.AtividadeEmissoraMapper;
import com.greenew.relatorios.repository.AtividadeEmissoraRepository;
import com.greenew.relatorios.repository.FatorEmissaoRepository;
import com.greenew.relatorios.repository.RelatorioGHGRepository;
import com.greenew.relatorios.service.AtividadeEmissoraService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class AtividadeEmissoraServiceImpl implements AtividadeEmissoraService {

    private final AtividadeEmissoraRepository atividadeRepository;
    private final RelatorioGHGRepository relatorioGHGRepository;
    private final AtividadeEmissoraMapper atividadeMapper;
    private final FatorEmissaoRepository fatorEmissaoRepository;

    public AtividadeEmissoraServiceImpl(AtividadeEmissoraRepository aRepo, RelatorioGHGRepository rRepo, AtividadeEmissoraMapper aMapper, FatorEmissaoRepository fRepo) {
        this.atividadeRepository = aRepo;
        this.relatorioGHGRepository = rRepo;
        this.atividadeMapper = aMapper;
        this.fatorEmissaoRepository = fRepo;
    }

    @Override
    public AtividadeEmissoraResponseDTO adicionarAtividade(UUID relatorioId, AtividadeEmissoraRequestDTO requestDTO) {
        // 1. Busca o Relatório (pai)
        RelatorioGHGEntity relatorio = relatorioGHGRepository.findById(relatorioId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Relatório GHG não encontrado com ID: " + relatorioId));

        // 2. Busca o Fator de Emissão (dado mestre)
        FatorEmissaoEntity fator = fatorEmissaoRepository.findById(requestDTO.getFatorEmissaoId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Fator de Emissão não encontrado com ID: " + requestDTO.getFatorEmissaoId()));

        // 3. Cria a nova Atividade
        AtividadeEmissoraEntity novaAtividade = new AtividadeEmissoraEntity();
        novaAtividade.setRelatorio(relatorio);
        novaAtividade.setQuantidade(requestDTO.getQuantidade());

        // 4. Faz o "Snapshot" dos dados do Fator para a Atividade
        novaAtividade.setNome(fator.getNomeAtividade());
        novaAtividade.setUnidade(fator.getUnidade());
        novaAtividade.setEscopo(fator.getEscopo());
        novaAtividade.setCategoriaEscopo3(fator.getCategoriaEscopo3());

        AtividadeEmissoraEntity atividadeSalva = atividadeRepository.save(novaAtividade);
        return atividadeMapper.toResponseDTO(atividadeSalva);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AtividadeEmissoraResponseDTO> buscarAtividadesPorRelatorioId(UUID relatorioId) {
        if (!relatorioGHGRepository.existsById(relatorioId)) {
            throw new RecursoNaoEncontradoException("Relatório GHG não encontrado com ID: " + relatorioId);
        }
        // Você precisará criar este método no AtividadeEmissoraRepository
        return atividadeRepository.findAllByRelatorioId(relatorioId).stream()
                .map(atividadeMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public AtividadeEmissoraResponseDTO buscarAtividadePorId(UUID atividadeId) {
        return atividadeRepository.findById(atividadeId)
                .map(atividadeMapper::toResponseDTO)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Atividade de Emissão não encontrada com ID: " + atividadeId));
    }

    @Override
    public AtividadeEmissoraResponseDTO atualizarAtividade(UUID atividadeId, AtividadeEmissoraRequestDTO requestDTO) {
        AtividadeEmissoraEntity entity = atividadeRepository.findById(atividadeId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Atividade de Emissão não encontrada com ID: " + atividadeId));

        // Busca o novo fator de emissão
        FatorEmissaoEntity fator = fatorEmissaoRepository.findById(requestDTO.getFatorEmissaoId())
                .orElseThrow(() -> new RecursoNaoEncontradoException("Fator de Emissão não encontrado com ID: " + requestDTO.getFatorEmissaoId()));

        // Atualiza os dados
        entity.setQuantidade(requestDTO.getQuantidade());
        entity.setNome(fator.getNomeAtividade());
        entity.setUnidade(fator.getUnidade());
        entity.setEscopo(fator.getEscopo());
        entity.setCategoriaEscopo3(fator.getCategoriaEscopo3());

        return atividadeMapper.toResponseDTO(atividadeRepository.save(entity));
    }

    @Override
    public void deletarAtividade(UUID atividadeId) {
        if (!atividadeRepository.existsById(atividadeId)) {
            throw new RecursoNaoEncontradoException("Atividade de Emissão não encontrada com ID: " + atividadeId);
        }
        atividadeRepository.deleteById(atividadeId);
    }
}