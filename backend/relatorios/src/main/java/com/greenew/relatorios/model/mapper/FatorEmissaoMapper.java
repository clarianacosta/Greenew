package com.greenew.relatorios.model.mapper;

import com.greenew.relatorios.model.dto.FatorEmissaoRequestDTO;
import com.greenew.relatorios.model.dto.FatorEmissaoResponseDTO;
import com.greenew.relatorios.model.entity.FatorEmissaoEntity;
import org.springframework.stereotype.Component;

@Component
public class FatorEmissaoMapper {

    public FatorEmissaoResponseDTO toResponseDTO(FatorEmissaoEntity entity) {
        if (entity == null) {
            return null;
        }
        FatorEmissaoResponseDTO dto = new FatorEmissaoResponseDTO();
        dto.setId(entity.getId());
        dto.setNomeAtividade(entity.getNomeAtividade());
        dto.setUnidade(entity.getUnidade());
        dto.setFatorCo2(entity.getFatorCo2());
        dto.setFatorCh4(entity.getFatorCh4());
        dto.setFatorN2o(entity.getFatorN2o());
        dto.setFonte(entity.getFonte());
        return dto;
    }

    public FatorEmissaoEntity toEntity(FatorEmissaoRequestDTO dto) {
        if (dto == null) {
            return null;
        }
        FatorEmissaoEntity entity = new FatorEmissaoEntity();
        entity.setNomeAtividade(dto.getNomeAtividade());
        entity.setUnidade(dto.getUnidade());
        entity.setFatorCo2(dto.getFatorCo2());
        entity.setFatorCh4(dto.getFatorCh4());
        entity.setFatorN2o(dto.getFatorN2o());
        entity.setFonte(dto.getFonte());
        return entity;
    }

    public void updateEntityFromDTO(FatorEmissaoRequestDTO dto, FatorEmissaoEntity entity) {
        if (dto == null || entity == null) {
            return;
        }
        entity.setNomeAtividade(dto.getNomeAtividade());
        entity.setUnidade(dto.getUnidade());
        entity.setFatorCo2(dto.getFatorCo2());
        entity.setFatorCh4(dto.getFatorCh4());
        entity.setFatorN2o(dto.getFatorN2o());
        entity.setFonte(dto.getFonte());
    }
}