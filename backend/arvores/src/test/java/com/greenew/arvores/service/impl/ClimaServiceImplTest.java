package com.greenew.arvores.service.impl;
import java.util.UUID;

import com.greenew.arvores.exception.RecursoNaoEncontradoException;
import com.greenew.arvores.model.dto.ClimaRequestDTO;
import com.greenew.arvores.model.dto.ClimaResponseDTO;
import com.greenew.arvores.model.entity.ClimaEntity;
import com.greenew.arvores.model.mapper.ClimaMapper;
import com.greenew.arvores.repository.ClimaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes para a classe ClimaServiceImpl")
class ClimaServiceImplTest {

    @Mock
    private ClimaRepository climaRepository;

    @Mock
    private ClimaMapper climaMapper;

    @InjectMocks
    private ClimaServiceImpl climaService;

    private ClimaEntity climaEntity;
    private ClimaRequestDTO climaRequestDTO; // NOVO: Para entrada
    private ClimaResponseDTO climaResponseDTO;
    private UUID climaId;

    @BeforeEach
    void setUp() {
        climaId = UUID.randomUUID();
        climaEntity = new ClimaEntity();
        climaEntity.setId(climaId);
        climaEntity.setNome("Tropical");
        climaEntity.setDescricao("Clima quente e úmido.");

        climaRequestDTO = new ClimaRequestDTO("Tropical", "Clima quente e úmido.");

        climaResponseDTO = new ClimaResponseDTO();
        climaResponseDTO.setId(climaId);
        climaResponseDTO.setNome("Tropical");
        climaResponseDTO.setDescricao("Clima quente e úmido.");
    }

    @Test
    @DisplayName("Deve criar um clima com sucesso")
    void criar_Success() {
        when(climaMapper.toEntity(any(ClimaRequestDTO.class))).thenReturn(climaEntity); // Usa RequestDTO
        when(climaRepository.save(any(ClimaEntity.class))).thenReturn(climaEntity);
        when(climaMapper.toResponseDTO(any(ClimaEntity.class))).thenReturn(climaResponseDTO); // Usa toResponseDTO

        ClimaResponseDTO resultado = climaService.criar(climaRequestDTO); // Usa RequestDTO

        assertThat(resultado).isEqualTo(climaResponseDTO);
        verify(climaRepository).save(climaEntity);
    }

    @Test
    @DisplayName("Deve buscar um clima por ID com sucesso")
    void buscarPorId_Success() {
        when(climaRepository.findById(climaId)).thenReturn(Optional.of(climaEntity));
        when(climaMapper.toResponseDTO(any(ClimaEntity.class))).thenReturn(climaResponseDTO); // Usa toResponseDTO

        ClimaResponseDTO resultado = climaService.buscarPorId(climaId);

        assertThat(resultado).isEqualTo(climaResponseDTO);
    }

    @Test
    @DisplayName("Deve buscar a Entity por ID com sucesso para uso interno")
    void getEntityById_Success() {
        when(climaRepository.findById(climaId)).thenReturn(Optional.of(climaEntity));

        ClimaEntity resultado = climaService.getEntityById(climaId); // Chama getEntityById

        assertThat(resultado).isEqualTo(climaEntity);
    }

    @Test
    @DisplayName("Deve lançar RecursoNaoEncontradoException ao buscar Entity por ID inexistente")
    void getEntityById_NotFound() {
        when(climaRepository.findById(climaId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> climaService.getEntityById(climaId))
                .isInstanceOf(RecursoNaoEncontradoException.class)
                .hasMessage("Clima não encontrado com ID: " + climaId);
    }

    @Test
    @DisplayName("Deve buscar todos os climas com sucesso")
    void buscarTodos_Success() {
        List<ClimaEntity> climas = List.of(climaEntity);
        when(climaRepository.findAll()).thenReturn(climas);
        when(climaMapper.toResponseDTO(any(ClimaEntity.class))).thenReturn(climaResponseDTO); // Usa toResponseDTO

        List<ClimaResponseDTO> resultado = climaService.buscarTodos();

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0)).isEqualTo(climaResponseDTO);
    }

    @Test
    @DisplayName("Deve atualizar um clima existente com sucesso")
    void atualizar_Success() {
        when(climaRepository.findById(climaId)).thenReturn(Optional.of(climaEntity));

        // Mocking the updateEntityFromDTO logic
        doNothing().when(climaMapper).updateEntityFromDTO(any(ClimaRequestDTO.class), eq(climaEntity));

        when(climaRepository.save(any(ClimaEntity.class))).thenReturn(climaEntity);
        when(climaMapper.toResponseDTO(any(ClimaEntity.class))).thenReturn(climaResponseDTO);

        ClimaResponseDTO resultado = climaService.atualizar(climaId, climaRequestDTO); // Usa RequestDTO

        assertThat(resultado).isEqualTo(climaResponseDTO);
        verify(climaRepository).save(climaEntity);
    }

    @Test
    @DisplayName("Deve lançar RecursoNaoEncontradoException ao tentar atualizar clima inexistente")
    void atualizar_NotFound() {
        when(climaRepository.findById(climaId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> climaService.atualizar(climaId, climaRequestDTO)) // Usa RequestDTO
                .isInstanceOf(RecursoNaoEncontradoException.class)
                .hasMessage("Clima não encontrado com ID: " + climaId);
    }

    @Test
    @DisplayName("Deve deletar um clima com sucesso")
    void deletar_Success() {
        when(climaRepository.existsById(climaId)).thenReturn(true);
        doNothing().when(climaRepository).deleteById(climaId);

        climaService.deletar(climaId);

        verify(climaRepository).deleteById(climaId);
    }

    @Test
    @DisplayName("Deve lançar RecursoNaoEncontradoException ao tentar deletar clima inexistente")
    void deletar_NotFound() {
        when(climaRepository.existsById(climaId)).thenReturn(false);

        assertThatThrownBy(() -> climaService.deletar(climaId))
                .isInstanceOf(RecursoNaoEncontradoException.class)
                .hasMessage("Clima não encontrado com ID: " + climaId);
        verify(climaRepository, never()).deleteById(any(UUID.class));
    }
}