package com.greenew.relatorios.model.mapper;

import com.greenew.relatorios.model.dto.AtividadeEmissoraRequestDTO;
import com.greenew.relatorios.model.dto.AtividadeEmissoraResponseDTO;
import com.greenew.relatorios.model.entity.AtividadeEmissoraEntity;
import org.springframework.stereotype.Component;

@Component
public class AtividadeEmissoraMapper {

    public AtividadeEmissoraResponseDTO toResponseDTO(AtividadeEmissoraEntity entity) {
        if (entity == null) {
            return null;
        }
        AtividadeEmissoraResponseDTO dto = new AtividadeEmissoraResponseDTO();
        dto.setId(entity.getId());
        dto.setNome(entity.getNome());
        dto.setEscopo(entity.getEscopo());
        dto.setCategoriaEscopo3(entity.getCategoriaEscopo3());
        dto.setUnidade(entity.getUnidade());
        dto.setQuantidade(entity.getQuantidade());
        return dto;
    }

    public AtividadeEmissoraEntity toEntity(AtividadeEmissoraRequestDTO requestDTO) {
        if (requestDTO == null) {
            return null;
        }
        AtividadeEmissoraEntity entity = new AtividadeEmissoraEntity();
        entity.setNome(requestDTO.getNome());
        entity.setEscopo(requestDTO.getEscopo());
        entity.setCategoriaEscopo3(requestDTO.getCategoriaEscopo3());
        entity.setUnidade(requestDTO.getUnidade());
        entity.setQuantidade(requestDTO.getQuantidade());
        return entity;
    }
}