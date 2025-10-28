package com.greenew.produtores.model.mapper;

import com.greenew.produtores.model.dto.ProdutorRequestDTO;
import com.greenew.produtores.model.dto.ProdutorResponseDTO;
import com.greenew.produtores.model.entity.ProdutorEntity;
import org.springframework.stereotype.Component;
import java.util.stream.Collectors;

@Component
public class ProdutorMapper {

    private final TerrenoMapper terrenoMapper;

    public ProdutorMapper(TerrenoMapper terrenoMapper) {
        this.terrenoMapper = terrenoMapper;
    }

    public ProdutorResponseDTO toResponseDTO(ProdutorEntity entity) {
        if (entity == null) {
            return null;
        }

        ProdutorResponseDTO dto = new ProdutorResponseDTO();
        dto.setId(entity.getId());
        dto.setNomeCompleto(entity.getNomeCompleto());
        dto.setEmail(entity.getEmail());
        dto.setCelular(entity.getCelular());

        // Lógica corrigida: se a lista de terrenos da entidade não for nula, mapeia.
        // Se for nula, o campo no DTO permanecerá nulo.
        if (entity.getTerrenos() != null) {
            dto.setTerrenos(entity.getTerrenos().stream()
                    .map(terrenoMapper::toResponseDTO)
                    .collect(Collectors.toSet()));
        }
        return dto;
    }

    public ProdutorEntity toEntity(ProdutorRequestDTO requestDTO) {
        if (requestDTO == null) {
            return null;
        }
        ProdutorEntity entity = new ProdutorEntity();
        entity.setNomeCompleto(requestDTO.getNomeCompleto());
        entity.setEmail(requestDTO.getEmail());
        entity.setCelular(requestDTO.getCelular());
        return entity;
    }

    public void updateEntityFromDTO(ProdutorRequestDTO requestDTO, ProdutorEntity entity) {
        if (requestDTO == null || entity == null) {
            return;
        }
        entity.setNomeCompleto(requestDTO.getNomeCompleto());
        entity.setEmail(requestDTO.getEmail());
        entity.setCelular(requestDTO.getCelular());
    }
}