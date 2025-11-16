package com.greenew.produtores.model.mapper;

import com.greenew.produtores.model.dto.ProdutorResumeDTO;
import com.greenew.produtores.model.dto.TerrenoRequestDTO;
import com.greenew.produtores.model.dto.TerrenoResponseDTO;
import com.greenew.produtores.model.entity.ProdutorEntity;
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
        dto.setAreaDisponivelHectares(entity.getAreaDisponivelHectares());

        // Mapeia o produtor aninhado
        if (entity.getProdutor() != null) {
            dto.setProdutor(mapProdutorToResumoDTO(entity.getProdutor()));
        }

        // IDs de bioma/clima são preenchidos pelo Service
        return dto;
    }

    // Método auxiliar privado para o mapeamento do produtor
    private ProdutorResumeDTO mapProdutorToResumoDTO(ProdutorEntity produtor) {
        if (produtor == null) {
            return null;
        }
        return new ProdutorResumeDTO(
                produtor.getId(),
                produtor.getNomeCompleto(),
                produtor.getEmail(),
                produtor.getCelular()
        );
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
        entity.setAreaDisponivelHectares(requestDTO.getAreaDisponivelHectares());
        // O relacionamento com o produtor é estabelecido na camada de serviço, por isso não é mapeado aqui.
        return entity;
    }
}