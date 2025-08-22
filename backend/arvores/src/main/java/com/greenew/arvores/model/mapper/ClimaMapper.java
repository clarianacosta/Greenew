package com.greenew.arvores.model.mapper;

import com.greenew.arvores.model.dto.ClimaDTO;
import com.greenew.arvores.model.entity.ClimaEntity;
import org.springframework.stereotype.Component;

@Component
public class ClimaMapper {
    public ClimaDTO toDTO(ClimaEntity clima) {
        if (clima == null) {
            return null;
        }
        ClimaDTO dto = new ClimaDTO();
        dto.setId(clima.getId());
        dto.setNome(clima.getNome());
        dto.setDescricao(clima.getDescricao());
        return dto;
    }

    public ClimaEntity toEntity(ClimaDTO climaDTO) {
        if (climaDTO == null) {
            return null;
        }
        ClimaEntity entity = new ClimaEntity();
        entity.setId(climaDTO.getId());
        entity.setNome(climaDTO.getNome());
        entity.setDescricao(climaDTO.getDescricao());
        return entity;
    }
}