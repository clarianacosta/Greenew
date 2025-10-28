package com.greenew.produtores.model.mapper;

import com.greenew.produtores.model.dto.TerrenoRequestDTO;
import com.greenew.produtores.model.dto.TerrenoResponseDTO;
import com.greenew.produtores.model.entity.TerrenoEntity;
import org.springframework.stereotype.Component;

@Component
public class TerrenoMapper {
    public TerrenoResponseDTO toResponseDTO(TerrenoEntity entity) {
        if (entity == null) {
            return null;
        }

        TerrenoResponseDTO dto = new TerrenoResponseDTO();
        dto.setId(entity.getId());
        dto.setLatitude(entity.getLatitude());
        dto.setLongitude(entity.getLongitude());
//        dto.setBiomaIdLocal(entity.getBiomaIdLocal());
//        dto.setClimaIdLocal(entity.getClimaIdLocal());
        dto.setProdutorId(entity.getProdutor().getId());
        return dto;
    }

    public TerrenoEntity toEntity(TerrenoRequestDTO requestDTO) {
        if (requestDTO == null) {
            return null;
        }

        TerrenoEntity entity = new TerrenoEntity();
        entity.setLatitude(requestDTO.getLatitude());
        entity.setLongitude(requestDTO.getLongitude());
        entity.setBiomaIdLocal(requestDTO.getBiomaIdLocal());
        entity.setClimaIdLocal(requestDTO.getClimaIdLocal());
        // O relacionamento com o produtor é estabelecido na camada de serviço, por isso não é mapeado aqui.
        return entity;
    }
}