package com.greenew.produtores.model.mapper;

import com.greenew.produtores.model.dto.ProdutorRequestDTO;
import com.greenew.produtores.model.dto.ProdutorResponseDTO;
import com.greenew.produtores.model.dto.TerrenoResponseDTO;
import com.greenew.produtores.model.entity.ProdutorEntity;
import com.greenew.produtores.model.entity.TerrenoEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes para a classe ProdutorMapper")
class ProdutorMapperTest {

    @Mock
    private TerrenoMapper terrenoMapper;

    @InjectMocks
    private ProdutorMapper produtorMapper;

    private ProdutorEntity produtorEntity;
    private ProdutorRequestDTO produtorRequestDTO;
    private UUID produtorId;

    @BeforeEach
    void setUp() {
        produtorId = UUID.randomUUID();

        produtorEntity = new ProdutorEntity();
        produtorEntity.setId(produtorId);
        produtorEntity.setNomeCompleto("Ana Teste");
        produtorEntity.setEmail("ana@teste.com");
        produtorEntity.setCelular("11911112222");

        produtorRequestDTO = new ProdutorRequestDTO("Ana Teste", "ana@teste.com", "11911112222");
    }

    @Test
    @DisplayName("Deve mapear ProdutorEntity para ProdutorResponseDTO quando terrenos é null")
    void toResponseDTO_NullTerrenos() {
        produtorEntity.setTerrenos(null);
        ProdutorResponseDTO dto = produtorMapper.toResponseDTO(produtorEntity);
        assertThat(dto).isNotNull();
        assertThat(dto.getTerrenos()).isNull();
    }

    @Test
    @DisplayName("Deve mapear ProdutorEntity para ProdutorResponseDTO sem terrenos (Set VAZIO)")
    void toResponseDTO_WithoutTerrenos() {
        produtorEntity.setTerrenos(Collections.emptySet());
        ProdutorResponseDTO dto = produtorMapper.toResponseDTO(produtorEntity);
        assertThat(dto).isNotNull();
        assertThat(dto.getTerrenos()).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("Deve mapear ProdutorEntity para ProdutorResponseDTO com terrenos")
    void toResponseDTO_WithTerrenos() {
        TerrenoEntity terrenoEntity = new TerrenoEntity();
        terrenoEntity.setId(UUID.randomUUID());
        Set<TerrenoEntity> terrenos = Set.of(terrenoEntity);
        produtorEntity.setTerrenos(terrenos);

        TerrenoResponseDTO terrenoDTO = new TerrenoResponseDTO();
        terrenoDTO.setId(terrenoEntity.getId());

        when(terrenoMapper.toResponseDTO(terrenoEntity)).thenReturn(terrenoDTO);

        ProdutorResponseDTO dto = produtorMapper.toResponseDTO(produtorEntity);

        assertThat(dto).isNotNull();
        assertThat(dto.getNomeCompleto()).isEqualTo("Ana Teste");
        assertThat(dto.getTerrenos()).hasSize(1);
        assertThat(dto.getTerrenos().iterator().next().getId()).isEqualTo(terrenoEntity.getId());
    }

    @Test
    @DisplayName("Deve retornar null ao mapear uma ProdutorEntity nula")
    void toResponseDTO_NullEntity() {
        ProdutorResponseDTO dto = produtorMapper.toResponseDTO(null);
        assertThat(dto).isNull();
    }

    @Test
    @DisplayName("Deve mapear ProdutorRequestDTO para ProdutorEntity com sucesso")
    void toEntity_Success() {
        ProdutorEntity entity = produtorMapper.toEntity(produtorRequestDTO);

        assertThat(entity).isNotNull();
        assertThat(entity.getNomeCompleto()).isEqualTo("Ana Teste");
        assertThat(entity.getId()).isNull();
    }

    @Test
    @DisplayName("Deve retornar null ao mapear um ProdutorRequestDTO nulo")
    void toEntity_NullDTO() {
        ProdutorEntity entity = produtorMapper.toEntity(null);
        assertThat(entity).isNull();
    }

    @Test
    @DisplayName("Deve atualizar ProdutorEntity a partir do RequestDTO")
    void updateEntityFromDTO_Success() {
        ProdutorRequestDTO updateDTO = new ProdutorRequestDTO("Novo Nome", "novo@teste.com", "999999999");
        produtorMapper.updateEntityFromDTO(updateDTO, produtorEntity);
        assertThat(produtorEntity.getNomeCompleto()).isEqualTo("Novo Nome");
        assertThat(produtorEntity.getEmail()).isEqualTo("novo@teste.com");
        assertThat(produtorEntity.getId()).isEqualTo(produtorId);
    }
}