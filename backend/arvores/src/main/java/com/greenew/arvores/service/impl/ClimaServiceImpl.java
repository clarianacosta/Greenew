package com.greenew.arvores.service.impl;

import com.greenew.arvores.exception.RecursoNaoEncontradoException;
import com.greenew.arvores.model.dto.ClimaRequestDTO; // NOVO: Para mapeamento de entrada
import com.greenew.arvores.model.dto.ClimaResponseDTO;
import com.greenew.arvores.model.entity.ClimaEntity;
import com.greenew.arvores.model.mapper.ClimaMapper;
import com.greenew.arvores.repository.ClimaRepository;
import com.greenew.arvores.service.ClimaService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ClimaServiceImpl implements ClimaService {

    private final ClimaRepository climaRepository;
    private final ClimaMapper climaMapper;

    public ClimaServiceImpl(ClimaRepository climaRepository, ClimaMapper climaMapper) {
        this.climaRepository = climaRepository;
        this.climaMapper = climaMapper;
    }

    /** Busca a Entity ou lança RecursoNaoEncontradoException. Usada internamente. */
    private ClimaEntity findEntityOrThrow(UUID id) {
        return climaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Clima não encontrado com ID: " + id));
    }

    // --- CREATE (Usando RequestDTO) ---
    @Override
    public ClimaResponseDTO criar(ClimaRequestDTO climaRequestDTO) {
        ClimaEntity clima = climaMapper.toEntity(climaRequestDTO);
        return climaMapper.toResponseDTO(climaRepository.save(clima));
    }

    // --- READ (Busca e retorna ResponseDTO) ---
    @Override
    public ClimaResponseDTO buscarPorId(UUID id) {
        ClimaEntity clima = findEntityOrThrow(id);
        return climaMapper.toResponseDTO(clima);
    }

    // --- READ (Método interno para ArvoreService) ---
    @Override
    public ClimaEntity getEntityById(UUID id) {
        return findEntityOrThrow(id);
    }

    @Override
    public List<ClimaResponseDTO> buscarTodos() {
        return climaRepository.findAll().stream()
                .map(climaMapper::toResponseDTO) // Renomeamos toDTO para toResponseDTO
                .collect(Collectors.toList());
    }

    // --- UPDATE (Usando RequestDTO) ---
    @Override
    public ClimaResponseDTO atualizar(UUID id, ClimaRequestDTO climaRequestDTO) {
        ClimaEntity climaExistente = findEntityOrThrow(id);

        // Usa o método do Mapper para atualizar a Entity
        climaMapper.updateEntityFromDTO(climaRequestDTO, climaExistente);

        return climaMapper.toResponseDTO(climaRepository.save(climaExistente));
    }

    // --- DELETE ---
    @Override
    public void deletar(UUID id) {
        if (!climaRepository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Clima não encontrado com ID: " + id);
        }
        climaRepository.deleteById(id);
    }
}