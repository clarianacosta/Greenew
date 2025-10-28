package com.greenew.empresas.model.mapper;

import com.greenew.empresas.model.dto.EmpresaRequestDTO;
import com.greenew.empresas.model.dto.EmpresaResponseDTO;
import com.greenew.empresas.model.entity.EmpresaEntity;
import org.springframework.stereotype.Component;

/**
 * Componente responsável por converter objetos entre Entity e DTO para o recurso Empresa.
 * Versão simplificada para o contexto do microserviço focado apenas em empresas.
 */
@Component
public class EmpresaMapper {

    /**
     * Converte uma EmpresaEntity para um EmpresaResponseDTO.
     *
     * @param entity A entidade a ser convertida.
     * @return O DTO correspondente para a API.
     */
    public EmpresaResponseDTO toResponseDTO(EmpresaEntity entity) {
        if (entity == null) {
            return null;
        }

        EmpresaResponseDTO dto = new EmpresaResponseDTO();
        dto.setId(entity.getId());
        dto.setRazaoSocial(entity.getRazaoSocial());
        dto.setCnpj(entity.getCnpj());

        return dto;
    }

    /**
     * Converte um EmpresaRequestDTO para uma nova EmpresaEntity.
     *
     * @param requestDTO O DTO recebido na requisição.
     * @return Uma nova entidade, pronta para ser salva no banco.
     */
    public EmpresaEntity toEntity(EmpresaRequestDTO requestDTO) {
        if (requestDTO == null) {
            return null;
        }

        EmpresaEntity entity = new EmpresaEntity();
        entity.setRazaoSocial(requestDTO.getRazaoSocial());
        entity.setCnpj(requestDTO.getCnpj());

        return entity;
    }

    /**
     * Atualiza uma entidade existente com os dados de um DTO.
     *
     * @param requestDTO O DTO com os novos dados.
     * @param entity     A entidade a ser atualizada.
     */
    public void updateEntityFromDTO(EmpresaRequestDTO requestDTO, EmpresaEntity entity) {
        if (requestDTO == null || entity == null) {
            return;
        }

        entity.setRazaoSocial(requestDTO.getRazaoSocial());
        entity.setCnpj(requestDTO.getCnpj());
    }
}