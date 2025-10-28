package com.greenew.arvores.service.impl;

import com.greenew.arvores.exception.RecursoNaoEncontradoException;
import com.greenew.arvores.model.dto.BiomaRequestDTO; // NOVO: Para mapeamento de entrada
import com.greenew.arvores.model.dto.BiomaResponseDTO;
import com.greenew.arvores.model.entity.BiomaEntity;
import com.greenew.arvores.model.mapper.BiomaMapper;
import com.greenew.arvores.repository.BiomaRepository;
import com.greenew.arvores.service.BiomaService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BiomaServiceImpl implements BiomaService {

    private final BiomaRepository biomaRepository;
    private final BiomaMapper biomaMapper;

    public BiomaServiceImpl(BiomaRepository biomaRepository, BiomaMapper biomaMapper) {
        this.biomaRepository = biomaRepository;
        this.biomaMapper = biomaMapper;
    }

    /** Busca a Entity ou lança RecursoNaoEncontradoException. Usada internamente. */
    private BiomaEntity findEntityOrThrow(UUID id) {
        return biomaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Bioma não encontrado com ID: " + id));
    }

    // --- CREATE (Usando RequestDTO) ---
    @Override
    public BiomaResponseDTO criar(BiomaRequestDTO biomaRequestDTO) {
        BiomaEntity bioma = biomaMapper.toEntity(biomaRequestDTO);
        return biomaMapper.toResponseDTO(biomaRepository.save(bioma));
    }

    // --- READ (Busca e retorna ResponseDTO) ---
    @Override
    public BiomaResponseDTO buscarPorId(UUID id) {
        BiomaEntity bioma = findEntityOrThrow(id);
        return biomaMapper.toResponseDTO(bioma);
    }

    // --- READ (Método interno para ArvoreService) ---
    @Override
    public BiomaEntity getEntityById(UUID id) {
        return findEntityOrThrow(id);
    }

    @Override
    public List<BiomaResponseDTO> buscarTodos() {
        return biomaRepository.findAll().stream()
                .map(biomaMapper::toResponseDTO) // Renomeamos toDTO para toResponseDTO
                .collect(Collectors.toList());
    }

    // --- UPDATE (Usando RequestDTO) ---
    @Override
    public BiomaResponseDTO atualizar(UUID id, BiomaRequestDTO biomaRequestDTO) {
        BiomaEntity biomaExistente = findEntityOrThrow(id);

        // Usa o método do Mapper para atualizar a Entity (copia nome e descrição)
        biomaMapper.updateEntityFromDTO(biomaRequestDTO, biomaExistente);

        return biomaMapper.toResponseDTO(biomaRepository.save(biomaExistente));
    }

    // --- DELETE ---
    @Override
    public void deletar(UUID id) {
        if (!biomaRepository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Bioma não encontrado com ID: " + id);
        }
        biomaRepository.deleteById(id);
    }
}