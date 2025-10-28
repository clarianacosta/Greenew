package com.greenew.produtores.service.impl;

import com.greenew.produtores.exception.RecursoNaoEncontradoException;
import com.greenew.produtores.model.dto.ProdutorRequestDTO;
import com.greenew.produtores.model.dto.ProdutorResponseDTO;
import com.greenew.produtores.model.dto.TerrenoResponseDTO;
import com.greenew.produtores.model.entity.ProdutorEntity;
import com.greenew.produtores.model.entity.TerrenoEntity;
import com.greenew.produtores.model.mapper.ProdutorMapper;
import com.greenew.produtores.repository.ProdutorRepository;
import com.greenew.produtores.service.ProdutorService;
import com.greenew.produtores.service.TerrenoService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ProdutorServiceImpl implements ProdutorService {

    private final ProdutorRepository produtorRepository;
    private final ProdutorMapper produtorMapper;
    private final TerrenoService terrenoService;

    public ProdutorServiceImpl(ProdutorRepository produtorRepository, ProdutorMapper produtorMapper, TerrenoService terrenoService) {
        this.produtorRepository = produtorRepository;
        this.produtorMapper = produtorMapper;
        this.terrenoService = terrenoService;
    }

    private Set<TerrenoResponseDTO> getHydratedTerrenos(Set<TerrenoEntity> terrenos) {
        if (terrenos == null || terrenos.isEmpty()) {
            return Collections.emptySet();
        }
        return terrenos.stream()
                .map(terrenoService::mapEntityToFullResponseDTO)
                .collect(Collectors.toSet());
    }

    @Override
    @Transactional
    public ProdutorResponseDTO criar(ProdutorRequestDTO produtorRequestDTO) {
        // CORREÇÃO: Adicionada a validação de e-mail existente
        if (produtorRepository.existsByEmail(produtorRequestDTO.getEmail())) {
            throw new IllegalArgumentException("Já existe um produtor com este e-mail.");
        }
        ProdutorEntity entity = produtorMapper.toEntity(produtorRequestDTO);
        ProdutorEntity savedEntity = produtorRepository.save(entity);
        return produtorMapper.toResponseDTO(savedEntity);
    }

    @Override
    public ProdutorResponseDTO buscarPorId(UUID id) {
        ProdutorEntity produtor = produtorRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Produtor não encontrado com ID: " + id));

        ProdutorResponseDTO responseDTO = produtorMapper.toResponseDTO(produtor);

        if (responseDTO.getTerrenos() == null) {
            responseDTO.setTerrenos(getHydratedTerrenos(produtor.getTerrenos()));
        }
        return responseDTO;
    }

    @Override
    public List<ProdutorResponseDTO> buscarTodos() {
        return produtorRepository.findAll().stream()
                .map(produtorEntity -> {
                    ProdutorResponseDTO responseDTO = produtorMapper.toResponseDTO(produtorEntity);
                    responseDTO.setTerrenos(getHydratedTerrenos(produtorEntity.getTerrenos()));
                    return responseDTO;
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ProdutorResponseDTO atualizar(UUID id, ProdutorRequestDTO produtorRequestDTO) {
        ProdutorEntity produtorExistente = produtorRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Produtor não encontrado com ID: " + id));

        if (!produtorExistente.getEmail().equals(produtorRequestDTO.getEmail()) && produtorRepository.existsByEmail(produtorRequestDTO.getEmail())) {
            throw new IllegalArgumentException("O e-mail fornecido já está em uso por outro produtor.");
        }

        produtorMapper.updateEntityFromDTO(produtorRequestDTO, produtorExistente);
        ProdutorEntity produtorAtualizado = produtorRepository.save(produtorExistente);
        ProdutorResponseDTO responseDTO = produtorMapper.toResponseDTO(produtorAtualizado);
        responseDTO.setTerrenos(getHydratedTerrenos(produtorAtualizado.getTerrenos()));

        return responseDTO;
    }

    @Override
    @Transactional
    public void deletar(UUID id) {
        if (!produtorRepository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Produtor não encontrado com ID: " + id);
        }
        produtorRepository.deleteById(id);
    }
}