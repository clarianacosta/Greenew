package com.greenew.relatorios.service.impl;

import com.greenew.relatorios.exception.RecursoNaoEncontradoException;
import com.greenew.relatorios.model.dto.FatorEmissaoRequestDTO;
import com.greenew.relatorios.model.dto.FatorEmissaoResponseDTO;
import com.greenew.relatorios.model.entity.FatorEmissaoEntity;
import com.greenew.relatorios.model.mapper.FatorEmissaoMapper;
import com.greenew.relatorios.repository.FatorEmissaoRepository;
import com.greenew.relatorios.service.FatorEmissaoService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class FatorEmissaoServiceImpl implements FatorEmissaoService {

    private final FatorEmissaoRepository repository;
    private final FatorEmissaoMapper mapper;

    public FatorEmissaoServiceImpl(FatorEmissaoRepository repository, FatorEmissaoMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public FatorEmissaoResponseDTO criarFator(FatorEmissaoRequestDTO dto) {
        if(repository.existsByNomeAtividadeAndUnidade(dto.getNomeAtividade(), dto.getUnidade())) {
            throw new IllegalArgumentException("Fator de emissão com este nome e unidade já existe.");
        }
        FatorEmissaoEntity entity = mapper.toEntity(dto);
        return mapper.toResponseDTO(repository.save(entity));
    }

    @Override
    @Transactional(readOnly = true)
    public FatorEmissaoResponseDTO buscarPorId(UUID id) {
        return repository.findById(id)
                .map(mapper::toResponseDTO)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Fator de Emissão não encontrado com ID: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<FatorEmissaoResponseDTO> buscarTodos() {
        return repository.findAll().stream()
                .map(mapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    public FatorEmissaoResponseDTO atualizarFator(UUID id, FatorEmissaoRequestDTO dto) {
        FatorEmissaoEntity entity = repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Fator de Emissão não encontrado com ID: " + id));

        // Valida se a alteração de nome/unidade não cria uma duplicata
        if (!entity.getNomeAtividade().equals(dto.getNomeAtividade()) || !entity.getUnidade().equals(dto.getUnidade())) {
            if (repository.existsByNomeAtividadeAndUnidade(dto.getNomeAtividade(), dto.getUnidade())) {
                throw new IllegalArgumentException("Já existe um fator de emissão com o nome e unidade fornecidos.");
            }
        }

        mapper.updateEntityFromDTO(dto, entity);
        return mapper.toResponseDTO(repository.save(entity));
    }

    @Override
    public void deletarFator(UUID id) {
        if (!repository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Fator de Emissão não encontrado com ID: " + id);
        }
        repository.deleteById(id);
    }
}