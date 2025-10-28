package com.greenew.arvores.model.mapper;
import java.util.UUID;

import com.greenew.arvores.model.dto.ArvoreRequestDTO;
import com.greenew.arvores.model.dto.ArvoreResponseDTO;
import com.greenew.arvores.model.dto.BiomaResponseDTO;
import com.greenew.arvores.model.dto.ClimaResponseDTO;
import com.greenew.arvores.model.entity.ArvoreEntity;
import com.greenew.arvores.model.entity.ArvoresBiomasEntity;
import com.greenew.arvores.model.entity.ArvoresClimasEntity;
import com.greenew.arvores.model.entity.BiomaEntity;
import com.greenew.arvores.model.entity.ClimaEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes para a classe ArvoreMapper")
class ArvoreMapperTest {

    @Mock
    private BiomaMapper biomaMapper;

    @Mock
    private ClimaMapper climaMapper;

    @InjectMocks
    private ArvoreMapper arvoreMapper;

    private ArvoreEntity arvoreEntity;
    private ArvoreRequestDTO arvoreRequestDTO;
    private UUID arvoreId;

    @BeforeEach
    void setUp() {
        arvoreId = UUID.randomUUID();
        arvoreEntity = new ArvoreEntity();
        arvoreEntity.setId(arvoreId);
        arvoreEntity.setNomePopular("Ipê Amarelo");
        arvoreEntity.setNomeCientifico("Handroanthus albus");
        arvoreEntity.setTaxaAbsorcaoCo2Anual(new BigDecimal("22.5000"));

        arvoreRequestDTO = new ArvoreRequestDTO();
        arvoreRequestDTO.setNomePopular("Ipê Amarelo");
        arvoreRequestDTO.setNomeCientifico("Handroanthus albus");
        arvoreRequestDTO.setTaxaAbsorcaoCo2Anual(new BigDecimal("22.5000"));
    }

    // --- TESTES PARA TO RESPONSE DTO ---

    @Test
    @DisplayName("Deve mapear ArvoreEntity para ArvoreResponseDTO com sucesso")
    void toResponseDTO_Success() {
        Set<ArvoresBiomasEntity> biomasAssociados = new HashSet<>();
        Set<ArvoresClimasEntity> climasAssociados = new HashSet<>();

        BiomaEntity biomaEntity = new BiomaEntity();
        biomaEntity.setId(UUID.randomUUID());
        biomaEntity.setNome("Cerrado");
        ArvoresBiomasEntity arvoreBioma = new ArvoresBiomasEntity();
        arvoreBioma.setBioma(biomaEntity);
        biomasAssociados.add(arvoreBioma);

        ClimaEntity climaEntity = new ClimaEntity();
        climaEntity.setId(UUID.randomUUID());
        climaEntity.setNome("Tropical");
        ArvoresClimasEntity arvoreClima = new ArvoresClimasEntity();
        arvoreClima.setClima(climaEntity);
        climasAssociados.add(arvoreClima);

        // CORREÇÃO APLICADA: Chamando toResponseDTO (o novo método) em vez de toDTO
        when(biomaMapper.toResponseDTO(biomaEntity)).thenReturn(new BiomaResponseDTO(biomaEntity.getId(), biomaEntity.getNome(), biomaEntity.getDescricao()));
        when(climaMapper.toResponseDTO(climaEntity)).thenReturn(new ClimaResponseDTO(climaEntity.getId(), climaEntity.getNome(), climaEntity.getDescricao()));

        arvoreEntity.setBiomasAssociados(biomasAssociados);
        arvoreEntity.setClimasAssociados(climasAssociados);

        ArvoreResponseDTO responseDTO = arvoreMapper.toResponseDTO(arvoreEntity);

        assertThat(responseDTO).isNotNull();
        assertThat(responseDTO.getId()).isEqualTo(arvoreEntity.getId());
        assertThat(responseDTO.getNomePopular()).isEqualTo(arvoreEntity.getNomePopular());
        assertThat(responseDTO.getBiomas()).hasSize(1);
        assertThat(responseDTO.getClimas()).hasSize(1);
    }

    // Testes restantes permanecem iguais
    @Test
    @DisplayName("Deve retornar null ao mapear uma ArvoreEntity nula para ArvoreResponseDTO")
    void toResponseDTO_NullEntity() {
        ArvoreResponseDTO responseDTO = arvoreMapper.toResponseDTO(null);
        assertThat(responseDTO).isNull();
    }

    @Test
    @DisplayName("Deve mapear ArvoreEntity com sets de biomas e climas nulos")
    void toResponseDTO_NullSets() {
        arvoreEntity.setBiomasAssociados(null);
        arvoreEntity.setClimasAssociados(null);

        ArvoreResponseDTO responseDTO = arvoreMapper.toResponseDTO(arvoreEntity);

        assertThat(responseDTO).isNotNull();
        assertThat(responseDTO.getBiomas()).isEmpty();
        assertThat(responseDTO.getClimas()).isEmpty();
    }

    @Test
    @DisplayName("Deve mapear ArvoreRequestDTO para ArvoreEntity com sucesso")
    void toEntity_Success() {
        ArvoreEntity entity = arvoreMapper.toEntity(arvoreRequestDTO);

        assertThat(entity).isNotNull();
        assertThat(entity.getNomePopular()).isEqualTo(arvoreRequestDTO.getNomePopular());
        assertThat(entity.getNomeCientifico()).isEqualTo(arvoreRequestDTO.getNomeCientifico());
    }

    @Test
    @DisplayName("Deve retornar null ao mapear uma ArvoreRequestDTO nula para ArvoreEntity")
    void toEntity_NullDTO() {
        ArvoreEntity entity = arvoreMapper.toEntity(null);
        assertThat(entity).isNull();
    }
}