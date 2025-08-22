package com.greenew.arvores.service.impl;

import com.greenew.arvores.exception.RecursoNaoEncontradoException;
import com.greenew.arvores.model.dto.ArvoreRequestDTO;
import com.greenew.arvores.model.dto.ArvoreResponseDTO;
import com.greenew.arvores.model.entity.*;
import com.greenew.arvores.model.mapper.ArvoreMapper;
import com.greenew.arvores.repository.ArvoreRepository;
import com.greenew.arvores.service.ArvoreService;
import com.greenew.arvores.service.BiomaService;
import com.greenew.arvores.service.ClimaService;
import jakarta.transaction.Transactional;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ArvoreServiceImpl implements ArvoreService {

    private final ArvoreRepository arvoreRepository;
    private final BiomaService biomaService;
    private final ClimaService climaService;
    private final ArvoreMapper arvoreMapper;

    public ArvoreServiceImpl(
            ArvoreRepository arvoreRepository,
            BiomaService biomaService,
            ClimaService climaService,
            ArvoreMapper arvoreMapper) {
        this.arvoreRepository = arvoreRepository;
        this.biomaService = biomaService;
        this.climaService = climaService;
        this.arvoreMapper = arvoreMapper;
    }

    @Override
    @Transactional
    public ArvoreResponseDTO criar(ArvoreRequestDTO arvoreRequestDTO) {
        ArvoreEntity arvore = arvoreMapper.toEntity(arvoreRequestDTO);
        associarBiomasEClimas(arvore, arvoreRequestDTO.getBiomasIds(), arvoreRequestDTO.getClimasIds());
        return arvoreMapper.toResponseDTO(arvoreRepository.save(arvore));
    }

    @Override
    public ArvoreResponseDTO buscarPorId(Long id) {
        ArvoreEntity arvore = arvoreRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Árvore não encontrada com ID: " + id));
        return arvoreMapper.toResponseDTO(arvore);
    }

    @Override
    public List<ArvoreResponseDTO> buscarTodas() {
        return arvoreRepository.findAll().stream()
                .map(arvoreMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ArvoreResponseDTO atualizar(Long id, ArvoreRequestDTO arvoreRequestDTO) {
        ArvoreEntity arvoreExistente = arvoreRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Árvore não encontrada com ID: " + id));

        // Copia as propriedades do DTO para a entidade, ignorando os relacionamentos
        BeanUtils.copyProperties(arvoreRequestDTO, arvoreExistente, "id", "biomasAssociados", "climasAssociados");

        // Atualiza as associações
        associarBiomasEClimas(arvoreExistente, arvoreRequestDTO.getBiomasIds(), arvoreRequestDTO.getClimasIds());

        return arvoreMapper.toResponseDTO(arvoreRepository.save(arvoreExistente));
    }

    @Override
    @Transactional
    public void deletar(Long id) {
        if (!arvoreRepository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Árvore não encontrada com ID: " + id);
        }
        arvoreRepository.deleteById(id);
    }

    /**
     * Método auxiliar para buscar e associar Biomas e Climas à entidade de Árvore.
     * Esta lógica é reutilizada tanto na criação quanto na atualização.
     */
    private void associarBiomasEClimas(ArvoreEntity arvore, Set<Long> biomasIds, Set<Long> climasIds) {
        if (arvore.getBiomasAssociados() != null) {
            arvore.getBiomasAssociados().clear();
        }
        if (arvore.getClimasAssociados() != null) {
            arvore.getClimasAssociados().clear();
        }

        Set<ArvoresBiomasEntity> biomasAssociados = biomasIds.stream()
                .map(biomaId -> {
                    BiomaEntity bioma = biomaService.buscarPorId(biomaId)
                            .orElseThrow(() -> new RecursoNaoEncontradoException("Bioma não encontrado com ID: " + biomaId));

                    ArvoresBiomasEntity arvoreBioma = new ArvoresBiomasEntity();
                    arvoreBioma.setArvore(arvore);
                    arvoreBioma.setBioma(bioma);

                    arvoreBioma.setId(new ArvoresBiomasId(arvore.getId(), bioma.getId()));

                    return arvoreBioma;
                })
                .collect(Collectors.toSet());

        Set<ArvoresClimasEntity> climasAssociados = climasIds.stream()
                .map(climaId -> {
                    ClimaEntity clima = climaService.buscarPorId(climaId)
                            .orElseThrow(() -> new RecursoNaoEncontradoException("Clima não encontrado com ID: " + climaId));

                    ArvoresClimasEntity arvoreClima = new ArvoresClimasEntity();
                    arvoreClima.setArvore(arvore);
                    arvoreClima.setClima(clima);

                    arvoreClima.setId(new ArvoresClimasId(arvore.getId(), clima.getId()));

                    return arvoreClima;
                })
                .collect(Collectors.toSet());

        arvore.setBiomasAssociados(biomasAssociados);
        arvore.setClimasAssociados(climasAssociados);
    }
}