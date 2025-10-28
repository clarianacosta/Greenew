package com.greenew.arvores.model.mapper;

import com.greenew.arvores.model.dto.ClimaRequestDTO;
import com.greenew.arvores.model.dto.ClimaResponseDTO;
import com.greenew.arvores.model.entity.ClimaEntity;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class ClimaMapper {

    /**
     * Mapeia ClimaEntity para ClimaResponseDTO (Saída da API).
     */
    public ClimaResponseDTO toResponseDTO(ClimaEntity entity) {
        if (entity == null) {
            return null;
        }

        // Construtor completo: id, nome, descricao
        return new ClimaResponseDTO(
                entity.getId(),
                entity.getNome(),
                entity.getDescricao()
        );
    }

    /**
     * Mapeia ClimaRequestDTO para ClimaEntity (Entrada para persistência).
     */
    public ClimaEntity toEntity(ClimaRequestDTO requestDTO) {
        if (requestDTO == null) {
            return null;
        }

        ClimaEntity entity = new ClimaEntity();

        // O ID é ignorado, pois será gerado no POST.
        entity.setNome(requestDTO.getNome());
        entity.setDescricao(requestDTO.getDescricao());

        return entity;
    }

    /**
     * Atualiza uma ClimaEntity existente com dados do ClimaRequestDTO
     * (Usado em operações PUT).
     */
    public void updateEntityFromDTO(ClimaRequestDTO requestDTO, ClimaEntity entity) {
        if (requestDTO == null || entity == null) {
            return;
        }

        entity.setNome(requestDTO.getNome());
        entity.setDescricao(requestDTO.getDescricao());
        // O ID (e outros campos de controle, se existirem) não são modificados.
    }
}