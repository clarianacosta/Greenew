package com.greenew.arvores.model.mapper;

import com.greenew.arvores.model.dto.ArvoreRequestDTO;
import com.greenew.arvores.model.dto.ArvoreResponseDTO;
import com.greenew.arvores.model.dto.BiomaResponseDTO;
import com.greenew.arvores.model.dto.ClimaResponseDTO;
import com.greenew.arvores.model.entity.ArvoreEntity;
import com.greenew.arvores.model.entity.ArvoresBiomasEntity;
import com.greenew.arvores.model.entity.ArvoresClimasEntity;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ArvoreMapper {

    private final BiomaMapper biomaMapper;
    private final ClimaMapper climaMapper;

    // Injeção de dependência dos mappers de bioma e clima
    public ArvoreMapper(BiomaMapper biomaMapper, ClimaMapper climaMapper) {
        this.biomaMapper = biomaMapper;
        this.climaMapper = climaMapper;
    }

    // --- Mapeamento para RESPOSTA (Entity -> DTO) ---
    public ArvoreResponseDTO toResponseDTO(ArvoreEntity arvore) {
        if (arvore == null) {
            return null;
        }

        ArvoreResponseDTO responseDTO = new ArvoreResponseDTO();
        responseDTO.setId(arvore.getId());
        responseDTO.setNomePopular(arvore.getNomePopular());
        responseDTO.setNomeCientifico(arvore.getNomeCientifico());
        responseDTO.setTaxaAbsorcaoCo2Anual(arvore.getTaxaAbsorcaoCo2Anual());
        responseDTO.setCustoMedioMuda(arvore.getCustoMedioMuda());
        responseDTO.setTempoMaturidadeAnos(arvore.getTempoMaturidadeAnos());
        responseDTO.setAlturaMediaM(arvore.getAlturaMediaM());
        responseDTO.setDiametroCopaMedioM(arvore.getDiametroCopaMedioM());

        // Mapeia os sets de biomas e climas
        responseDTO.setBiomas(mapBiomas(arvore.getBiomasAssociados()));
        responseDTO.setClimas(mapClimas(arvore.getClimasAssociados()));

        return responseDTO;
    }

    // --- Mapeamento para REQUISIÇÃO (DTO -> Entity) ---
    public ArvoreEntity toEntity(ArvoreRequestDTO requestDTO) {
        if (requestDTO == null) {
            return null;
        }

        ArvoreEntity arvore = new ArvoreEntity();
        arvore.setNomePopular(requestDTO.getNomePopular());
        arvore.setNomeCientifico(requestDTO.getNomeCientifico());
        arvore.setTaxaAbsorcaoCo2Anual(requestDTO.getTaxaAbsorcaoCo2Anual());
        arvore.setCustoMedioMuda(requestDTO.getCustoMedioMuda());
        arvore.setTempoMaturidadeAnos(requestDTO.getTempoMaturidadeAnos());
        arvore.setAlturaMediaM(requestDTO.getAlturaMediaM());
        arvore.setDiametroCopaMedioM(requestDTO.getDiametroCopaMedioM());

        // Os conjuntos de biomas e climas são tratados na camada de serviço,
        // então não precisam de mapeamento aqui.
        return arvore;
    }

    // --- Métodos Auxiliares para conversão dos Sets ---
    private Set<BiomaResponseDTO> mapBiomas(Set<ArvoresBiomasEntity> biomasAssociados) {
        if (biomasAssociados == null) {
            return Collections.emptySet();
        }
        return biomasAssociados.stream()
                .map(ArvoresBiomasEntity::getBioma)
                // CORREÇÃO: Usar toResponseDTO conforme a nova padronização do mapper
                .map(biomaMapper::toResponseDTO)
                .collect(Collectors.toSet());
    }

    private Set<ClimaResponseDTO> mapClimas(Set<ArvoresClimasEntity> climasAssociados) {
        if (climasAssociados == null) {
            return Collections.emptySet();
        }
        return climasAssociados.stream()
                .map(ArvoresClimasEntity::getClima)
                // CORREÇÃO: Usar toResponseDTO conforme a nova padronização do mapper
                .map(climaMapper::toResponseDTO)
                .collect(Collectors.toSet());
    }
}