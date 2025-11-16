package com.greenew.produtores.model.mapper;

import com.greenew.produtores.model.dto.TerrenoRequestDTO;
import com.greenew.produtores.model.dto.TerrenoResponseDTO;
import com.greenew.produtores.model.entity.ProdutorEntity;
import com.greenew.produtores.model.entity.TerrenoEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Testes para a classe TerrenoMapper")
class TerrenoMapperTest {

    private TerrenoMapper terrenoMapper;
    private UUID terrenoId;
    private UUID produtorId;
    private UUID biomaId;
    private UUID climaId;
    private BigDecimal area;

    @BeforeEach
    void setUp() {
        terrenoMapper = new TerrenoMapper();
        terrenoId = UUID.randomUUID();
        produtorId = UUID.randomUUID();
        biomaId = UUID.randomUUID();
        climaId = UUID.randomUUID();
        area = new BigDecimal("50.5");
    }

    @Test
    @DisplayName("Deve mapear TerrenoEntity para TerrenoResponseDTO com sucesso")
    void toResponseDTO_Success() {
        // Cenário
        ProdutorEntity produtor = new ProdutorEntity();
        produtor.setId(produtorId);
        produtor.setNomeCompleto("Produtor Teste");
        produtor.setEmail("produtor@teste.com");

        TerrenoEntity entity = new TerrenoEntity();
        entity.setId(terrenoId);
        entity.setProdutor(produtor);
        entity.setLatitude(new BigDecimal("-15.78"));
        entity.setLongitude(new BigDecimal("-47.92"));
        entity.setBiomaIdLocal(biomaId);
        entity.setClimaIdLocal(climaId);
        entity.setAreaDisponivelHectares(area);

        // Ação
        TerrenoResponseDTO dto = terrenoMapper.toResponseDTO(entity);

        // Verificação
        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(terrenoId);
        assertThat(dto.getLatitude()).isEqualTo(new BigDecimal("-15.78"));
        assertThat(dto.getAreaDisponivelHectares()).isEqualTo(area);

        assertThat(dto.getProdutor()).isNotNull();
        assertThat(dto.getProdutor().getId()).isEqualTo(produtorId);
        assertThat(dto.getProdutor().getNomeCompleto()).isEqualTo("Produtor Teste");
    }

    @Test
    @DisplayName("Deve retornar null ao mapear uma TerrenoEntity nula")
    void toResponseDTO_NullEntity() {
        TerrenoResponseDTO dto = terrenoMapper.toResponseDTO(null);
        assertThat(dto).isNull();
    }

    @Test
    @DisplayName("Deve mapear TerrenoRequestDTO para TerrenoEntity com sucesso")
    void toEntity_Success() {
        // Cenário
        TerrenoRequestDTO dto = new TerrenoRequestDTO();
        dto.setLatitude(new BigDecimal("10.5"));
        dto.setLongitude(new BigDecimal("20.5"));
        dto.setBiomaIdLocal(biomaId);
        dto.setClimaIdLocal(climaId);
        dto.setAreaDisponivelHectares(area);
        // ProdutorId não é mapeado aqui

        // Ação
        TerrenoEntity entity = terrenoMapper.toEntity(dto);

        // Verificação
        assertThat(entity).isNotNull();
        assertThat(entity.getLatitude()).isEqualTo(new BigDecimal("10.5"));
        assertThat(entity.getBiomaIdLocal()).isEqualTo(biomaId);
        assertThat(entity.getAreaDisponivelHectares()).isEqualTo(area);
        assertThat(entity.getProdutor()).isNull(); // Deve ser nulo, pois é settado no service
    }

    @Test
    @DisplayName("Deve retornar null ao mapear um TerrenoRequestDTO nulo")
    void toEntity_NullDTO() {
        TerrenoEntity entity = terrenoMapper.toEntity(null);
        assertThat(entity).isNull();
    }
}