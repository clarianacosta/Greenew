package com.greenew.arvores.model.mapper;
import java.util.UUID;

import com.greenew.arvores.model.dto.BiomaRequestDTO;
import com.greenew.arvores.model.dto.BiomaResponseDTO;
import com.greenew.arvores.model.entity.BiomaEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Testes para a classe BiomaMapper")
class BiomaMapperTest {

    private BiomaMapper biomaMapper;

    @BeforeEach
    void setUp() {
        // Assume-se que o BiomaMapper foi corrigido para ter os métodos toResponseDTO e toEntity(BiomaRequestDTO)
        biomaMapper = new BiomaMapper();
    }

    // --- TESTES PARA CONVERSÃO DE SAÍDA (Entity -> ResponseDTO) ---

    @Test
    @DisplayName("Deve mapear BiomaEntity para BiomaResponseDTO com sucesso")
    void toResponseDTO_Success() {
        // Cenário de sucesso
        BiomaEntity entity = new BiomaEntity();
        entity.setId(UUID.randomUUID());
        entity.setNome("Cerrado");
        entity.setDescricao("Bioma brasileiro de savana tropical.");

        // Ação: Chama o método atualizado
        BiomaResponseDTO dto = biomaMapper.toResponseDTO(entity);

        // Verificação do mapeamento
        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(entity.getId());
        assertThat(dto.getNome()).isEqualTo("Cerrado");
        assertThat(dto.getDescricao()).isEqualTo(entity.getDescricao());
    }

    @Test
    @DisplayName("Deve retornar null ao mapear uma BiomaEntity nula")
    void toResponseDTO_NullEntity() {
        // Ação: Chama o método atualizado
        BiomaResponseDTO dto = biomaMapper.toResponseDTO(null);
        assertThat(dto).isNull();
    }

    // --- TESTES PARA CONVERSÃO DE ENTRADA (RequestDTO -> Entity) ---

    @Test
    @DisplayName("Deve mapear BiomaRequestDTO para BiomaEntity com sucesso")
    void toEntityFromRequest_Success() {
        // Cenário de sucesso: RequestDTO não contém o ID
        BiomaRequestDTO requestDTO = new BiomaRequestDTO("Caatinga", "Bioma de clima semiárido no Brasil.");

        // Ação: Chama o método atualizado que aceita RequestDTO
        BiomaEntity entity = biomaMapper.toEntity(requestDTO);

        // Verifica o mapeamento
        assertThat(entity).isNotNull();
        assertThat(entity.getNome()).isEqualTo("Caatinga");
        assertThat(entity.getDescricao()).isEqualTo(requestDTO.getDescricao());
        assertThat(entity.getId()).isNull(); // ID deve ser nulo no RequestDTO
    }

    @Test
    @DisplayName("Deve retornar null ao mapear um BiomaRequestDTO nulo")
    void toEntityFromRequest_NullDTO() {
        // Ação: Chama o método atualizado
        BiomaEntity entity = biomaMapper.toEntity(null);
        assertThat(entity).isNull();
    }

    // --- TESTES DE ATUALIZAÇÃO (RequestDTO -> Entity Existente) ---

    @Test
    @DisplayName("Deve atualizar BiomaEntity a partir do RequestDTO")
    void updateEntityFromDTO_Success() {
        UUID entityId = UUID.randomUUID();
        BiomaEntity entity = new BiomaEntity();
        entity.setId(entityId);
        entity.setNome("Cerrado Antigo");

        BiomaRequestDTO requestDTO = new BiomaRequestDTO("Cerrado Novo", "Nova descrição.");

        // Ação
        biomaMapper.updateEntityFromDTO(requestDTO, entity);

        // Verificação
        assertThat(entity.getId()).isEqualTo(entityId); // ID deve ser preservado
        assertThat(entity.getNome()).isEqualTo("Cerrado Novo");
        assertThat(entity.getDescricao()).isEqualTo("Nova descrição.");
    }
}