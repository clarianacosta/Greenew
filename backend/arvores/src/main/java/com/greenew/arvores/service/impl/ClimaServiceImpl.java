package com.greenew.arvores.service.impl;

import com.greenew.arvores.exception.RecursoNaoEncontradoException;
import com.greenew.arvores.model.dto.ClimaDTO;
import com.greenew.arvores.model.entity.ClimaEntity;
import com.greenew.arvores.model.mapper.ClimaMapper;
import com.greenew.arvores.repository.ClimaRepository;
import com.greenew.arvores.service.ClimaService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ClimaServiceImpl implements ClimaService {

    private final ClimaRepository climaRepository;
    private final ClimaMapper climaMapper;

    public ClimaServiceImpl(ClimaRepository climaRepository, ClimaMapper climaMapper) {
        this.climaRepository = climaRepository;
        this.climaMapper = climaMapper;
    }

    @Override
    public ClimaDTO criar(ClimaDTO climaDTO) {
        ClimaEntity clima = climaMapper.toEntity(climaDTO);
        return climaMapper.toDTO(climaRepository.save(clima));
    }

    @Override
    public Optional<ClimaEntity> buscarPorId(Long id) {
        return climaRepository.findById(id);
    }

    @Override
    public List<ClimaDTO> buscarTodos() {
        return climaRepository.findAll().stream()
                .map(climaMapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    public ClimaDTO atualizar(Long id, ClimaDTO climaDTO) {
        ClimaEntity climaExistente = climaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Clima não encontrado com ID: " + id));
        climaExistente.setNome(climaDTO.getNome());
        climaExistente.setDescricao(climaDTO.getDescricao());
        return climaMapper.toDTO(climaRepository.save(climaExistente));
    }

    @Override
    public void deletar(Long id) {
        if (!climaRepository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Clima não encontrado com ID: " + id);
        }
        climaRepository.deleteById(id);
    }
}