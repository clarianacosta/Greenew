package com.greenew.arvores.service.impl;

import com.greenew.arvores.exception.RecursoNaoEncontradoException;
import com.greenew.arvores.model.dto.BiomaDTO;
import com.greenew.arvores.model.entity.BiomaEntity;
import com.greenew.arvores.model.mapper.BiomaMapper;
import com.greenew.arvores.repository.BiomaRepository;
import com.greenew.arvores.service.BiomaService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BiomaServiceImpl implements BiomaService {

    private final BiomaRepository biomaRepository;
    private final BiomaMapper biomaMapper;

    public BiomaServiceImpl(BiomaRepository biomaRepository, BiomaMapper biomaMapper) {
        this.biomaRepository = biomaRepository;
        this.biomaMapper = biomaMapper;
    }

    @Override
    public BiomaDTO criar(BiomaDTO biomaDTO) {
        BiomaEntity bioma = biomaMapper.toEntity(biomaDTO);
        return biomaMapper.toDTO(biomaRepository.save(bioma));
    }

    @Override
    public Optional<BiomaEntity> buscarPorId(Long id) {
        return biomaRepository.findById(id);
    }

    @Override
    public List<BiomaDTO> buscarTodos() {
        return biomaRepository.findAll().stream()
                .map(biomaMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public BiomaDTO atualizar(Long id, BiomaDTO biomaDTO) {
        BiomaEntity biomaExistente = biomaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Bioma não encontrado com ID: " + id));
        biomaExistente.setNome(biomaDTO.getNome());
        biomaExistente.setDescricao(biomaDTO.getDescricao());
        return biomaMapper.toDTO(biomaRepository.save(biomaExistente));
    }

    @Override
    public void deletar(Long id) {
        if (!biomaRepository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Bioma não encontrado com ID: " + id);
        }
        biomaRepository.deleteById(id);
    }
}