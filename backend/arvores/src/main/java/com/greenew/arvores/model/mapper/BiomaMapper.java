package com.greenew.arvores.model.mapper;

import com.greenew.arvores.model.dto.BiomaRequestDTO;
import com.greenew.arvores.model.dto.BiomaResponseDTO;
import com.greenew.arvores.model.entity.BiomaEntity;
import org.springframework.stereotype.Component;

@Component
public class BiomaMapper {

    /**
     * Mapeia BiomaEntity para BiomaResponseDTO (Saída da API).
     */
    public BiomaResponseDTO toResponseDTO(BiomaEntity entity) {
        if (entity == null) {
            return null;
        }

        // Usando o construtor AllArgsConstructor se BiomaResponseDTO for imutável,
        // ou setters se for mutável (como no seu código):
        return new BiomaResponseDTO(
                entity.getId(),
                entity.getNome(),
                entity.getDescricao()
        );
    }

    /**
     * Mapeia BiomaRequestDTO para BiomaEntity (Entrada para persistência).
     */
    public BiomaEntity toEntity(BiomaRequestDTO requestDTO) {
        if (requestDTO == null) {
            return null;
        }

        BiomaEntity entity = new BiomaEntity();

        // O ID não é mapeado do RequestDTO, pois será gerado pelo banco/service.
        entity.setNome(requestDTO.getNome());
        entity.setDescricao(requestDTO.getDescricao());

        return entity;
    }

    /**
     * Mapeia BiomaEntity para BiomaEntity (útil para o método PUT/atualização,
     * onde a Entity já existe e você só precisa copiar os campos do RequestDTO).
     */
    public void updateEntityFromDTO(BiomaRequestDTO requestDTO, BiomaEntity entity) {
        if (requestDTO == null || entity == null) {
            return;
        }

        entity.setNome(requestDTO.getNome());
        entity.setDescricao(requestDTO.getDescricao());
        // O ID da Entity existente não deve ser alterado aqui.
    }
}