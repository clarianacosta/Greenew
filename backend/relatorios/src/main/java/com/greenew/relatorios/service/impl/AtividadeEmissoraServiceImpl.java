package com.greenew.relatorios.service.impl;

import com.greenew.relatorios.exception.RecursoNaoEncontradoException;
import com.greenew.relatorios.model.dto.AtividadeEmissoraRequestDTO;
import com.greenew.relatorios.model.dto.AtividadeEmissoraResponseDTO;
import com.greenew.relatorios.model.entity.AtividadeEmissoraEntity;
import com.greenew.relatorios.model.entity.RelatorioGHGEntity;
import com.greenew.relatorios.model.mapper.AtividadeEmissoraMapper;
import com.greenew.relatorios.repository.AtividadeEmissoraRepository;
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

    public AtividadeEmissoraServiceImpl(AtividadeEmissoraRepository aRepo, RelatorioGHGRepository rRepo, AtividadeEmissoraMapper aMapper) {
        this.atividadeRepository = aRepo;
        this.relatorioGHGRepository = rRepo;
        this.atividadeMapper = aMapper;
    }

    @Override
    public AtividadeEmissoraResponseDTO adicionarAtividade(UUID relatorioId, AtividadeEmissoraRequestDTO requestDTO) {
        RelatorioGHGEntity relatorio = relatorioGHGRepository.findById(relatorioId)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Relatório GHG não encontrado com ID: " + relatorioId));

        AtividadeEmissoraEntity novaAtividade = atividadeMapper.toEntity(requestDTO);
        novaAtividade.setRelatorio(relatorio);

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

        // O mapper precisa de um método update
        // atividadeMapper.updateEntityFromDTO(requestDTO, entity);
        entity.setNome(requestDTO.getNome());
        entity.setEscopo(requestDTO.getEscopo());
        entity.setCategoriaEscopo3(requestDTO.getCategoriaEscopo3());
        entity.setUnidade(requestDTO.getUnidade());
        entity.setQuantidade(requestDTO.getQuantidade());

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