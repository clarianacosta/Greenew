package com.greenew.arvores.model.mapper;
import java.util.UUID;

import com.greenew.arvores.model.dto.ClimaRequestDTO;
import com.greenew.arvores.model.dto.ClimaResponseDTO;
import com.greenew.arvores.model.entity.ClimaEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Testes para a classe ClimaMapper")
class ClimaMapperTest {

    private ClimaMapper climaMapper;

    @BeforeEach
    void setUp() {
        climaMapper = new ClimaMapper();
    }

    // --- TESTES PARA CONVERSÃO DE SAÍDA (Entity -> ResponseDTO) ---

    @Test
    @DisplayName("Deve mapear ClimaEntity para ClimaResponseDTO com sucesso")
    void toResponseDTO_Success() {
        // Cenário de sucesso
        ClimaEntity entity = new ClimaEntity();
        entity.setId(UUID.randomUUID());
        entity.setNome("Tropical");
        entity.setDescricao("Clima quente e úmido.");

        // Ação: Chamando o método padronizado
        ClimaResponseDTO dto = climaMapper.toResponseDTO(entity);

        // Verifica o mapeamento
        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo(entity.getId());
        assertThat(dto.getNome()).isEqualTo(entity.getNome());
        assertThat(dto.getDescricao()).isEqualTo(entity.getDescricao());
    }

    @Test
    @DisplayName("Deve retornar null ao mapear uma ClimaEntity nula")
    void toResponseDTO_NullEntity() {
        // Ação: Chamando o método padronizado
        ClimaResponseDTO dto = climaMapper.toResponseDTO(null);
        assertThat(dto).isNull();
    }

    // --- TESTES PARA CONVERSÃO DE ENTRADA (RequestDTO -> Entity) ---

    @Test
    @DisplayName("Deve mapear ClimaRequestDTO para ClimaEntity com sucesso")
    void toEntityFromRequest_Success() {
        // Cenário de sucesso: Usando o RequestDTO (sem ID)
        ClimaRequestDTO requestDTO = new ClimaRequestDTO("Temperado", "Clima com estações bem definidas.");

        // Ação: Chamando o método atualizado que aceita RequestDTO
        ClimaEntity entity = climaMapper.toEntity(requestDTO);

        // Verifica o mapeamento
        assertThat(entity).isNotNull();
        assertThat(entity.getNome()).isEqualTo("Temperado");
        assertThat(entity.getDescricao()).isEqualTo(requestDTO.getDescricao());
        assertThat(entity.getId()).isNull(); // O ID deve ser nulo
    }

    @Test
    @DisplayName("Deve retornar null ao mapear um ClimaRequestDTO nulo")
    void toEntityFromRequest_NullDTO() {
        ClimaEntity entity = climaMapper.toEntity(null);
        assertThat(entity).isNull();
    }

    // --- TESTES DE ATUALIZAÇÃO ---

    @Test
    @DisplayName("Deve atualizar ClimaEntity a partir do ClimaRequestDTO")
    void updateEntityFromDTO_Success() {
        UUID entityId = UUID.randomUUID();
        ClimaEntity entity = new ClimaEntity();
        entity.setId(entityId);
        entity.setNome("Tropical Antigo");

        ClimaRequestDTO requestDTO = new ClimaRequestDTO("Tropical Novo", "Nova descrição de clima.");

        // Ação
        climaMapper.updateEntityFromDTO(requestDTO, entity);

        // Verificação
        assertThat(entity.getId()).isEqualTo(entityId); // ID deve ser preservado
        assertThat(entity.getNome()).isEqualTo("Tropical Novo");
        assertThat(entity.getDescricao()).isEqualTo("Nova descrição de clima.");
    }
}