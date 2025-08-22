package com.greenew.arvores.model.mapper;

import com.greenew.arvores.model.dto.BiomaDTO;
import com.greenew.arvores.model.entity.BiomaEntity;
import org.springframework.stereotype.Component;

@Component
public class BiomaMapper {
    public BiomaDTO toDTO(BiomaEntity bioma) {
        if (bioma == null) {
            return null;
        }
        BiomaDTO dto = new BiomaDTO();
        dto.setId(bioma.getId());
        dto.setNome(bioma.getNome());
        dto.setDescricao(bioma.getDescricao());
        return dto;
    }
    public BiomaEntity toEntity(BiomaDTO biomaDTO) {
        if (biomaDTO == null) {
            return null;
        }
        BiomaEntity entity = new BiomaEntity();
        entity.setId(biomaDTO.getId());
        entity.setNome(biomaDTO.getNome());
        entity.setDescricao(biomaDTO.getDescricao());
        return entity;
    }
}