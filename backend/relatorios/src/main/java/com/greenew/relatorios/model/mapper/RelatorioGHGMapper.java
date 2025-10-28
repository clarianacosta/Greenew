package com.greenew.relatorios.model.mapper;

import com.greenew.relatorios.model.dto.RelatorioGHGRequestDTO;
import com.greenew.relatorios.model.dto.RelatorioGHGResponseDTO;
import com.greenew.relatorios.model.entity.NivelCompletude;
import com.greenew.relatorios.model.entity.RelatorioGHGEntity;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.time.Year;

@Component
public class RelatorioGHGMapper {

    public RelatorioGHGResponseDTO toResponseDTO(RelatorioGHGEntity entity) {
        if (entity == null) {
            return null;
        }
        RelatorioGHGResponseDTO dto = new RelatorioGHGResponseDTO();
        dto.setId(entity.getId());
        dto.setAnoReferencia(entity.getAnoReferencia().getValue());
        dto.setEmissaoCalculadaCo2e(entity.getEmissaoCalculadaCo2e());
        dto.setNivel(entity.getNivel());
        dto.setArvoreRecomendada(entity.getArvoreRecomendada());
        dto.setQuantidadeNecessaria(entity.getQuantidadeNecessaria());
        dto.setCustoTotalEstimado(entity.getCustoTotalEstimado());
        return dto;
    }

    public RelatorioGHGEntity toEntity(RelatorioGHGRequestDTO requestDTO) {
        if (requestDTO == null) {
            return null;
        }
        RelatorioGHGEntity entity = new RelatorioGHGEntity();

        // Mapeia os dados do DTO
        entity.setEmpresaId(requestDTO.getEmpresaId());
        entity.setAnoReferencia(Year.of(requestDTO.getAnoReferencia()));

        // Define valores padrão para um novo relatório
        entity.setEmissaoCalculadaCo2e(BigDecimal.ZERO);
        entity.setNivel(NivelCompletude.OPERACIONAL);

        return entity;
    }
}