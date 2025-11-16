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
import java.util.UUID;
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
    public ArvoreResponseDTO buscarPorId(UUID id) {
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
    public ArvoreResponseDTO atualizar(UUID id, ArvoreRequestDTO arvoreRequestDTO) {
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
    public void deletar(UUID id) {
        if (!arvoreRepository.existsById(id)) {
            throw new RecursoNaoEncontradoException("Árvore não encontrada com ID: " + id);
        }
        arvoreRepository.deleteById(id);
    }

    /**
     * Método auxiliar para buscar e associar Biomas e Climas à entidade de Árvore.
     * Esta lógica é reutilizada tanto na criação quanto na atualização.
     */
    private void associarBiomasEClimas(ArvoreEntity arvore, Set<UUID> biomasIds, Set<UUID> climasIds) {
        // Limpa associações antigas (necessário para o 'atualizar')
        if (arvore.getBiomasAssociados() != null) {
            arvore.getBiomasAssociados().clear();
        } else {
            arvore.setBiomasAssociados(new java.util.HashSet<>());
        }

        if (arvore.getClimasAssociados() != null) {
            arvore.getClimasAssociados().clear();
        } else {
            arvore.setClimasAssociados(new java.util.HashSet<>());
        }

        // 1. Processa os Biomas
        Set<ArvoresBiomasEntity> biomasAssociados = biomasIds.stream()
                .map(biomaId -> {
                    // Busca a entidade Bioma
                    BiomaEntity bioma = biomaService.getEntityById(biomaId);

                    // Cria a entidade de junção
                    ArvoresBiomasEntity arvoreBioma = new ArvoresBiomasEntity();

                    // Define o ID composto. O 'arvore.getId()' será nulo no 'criar',
                    // mas será preenchido pelo @MapsId quando o 'arvore' for salvo.
                    arvoreBioma.setId(new ArvoresBiomasId(arvore.getId(), biomaId));

                    // Define os relacionamentos
                    arvoreBioma.setArvore(arvore);
                    arvoreBioma.setBioma(bioma);

                    return arvoreBioma;
                })
                .collect(Collectors.toSet());

        // 2. Processa os Climas
        Set<ArvoresClimasEntity> climasAssociados = climasIds.stream()
                .map(climaId -> {
                    // Busca a entidade Clima
                    ClimaEntity clima = climaService.getEntityById(climaId);

                    // Cria a entidade de junção
                    ArvoresClimasEntity arvoreClima = new ArvoresClimasEntity();

                    // Define o ID composto
                    arvoreClima.setId(new ArvoresClimasId(arvore.getId(), climaId));

                    // Define os relacionamentos
                    arvoreClima.setArvore(arvore);
                    arvoreClima.setClima(clima);

                    return arvoreClima;
                })
                .collect(Collectors.toSet());

        // Define os novos conjuntos na entidade Arvore
        arvore.getBiomasAssociados().addAll(biomasAssociados);
        arvore.getClimasAssociados().addAll(climasAssociados);
    }
}