package com.greenew.arvores.service.impl;
import java.util.UUID;

import com.greenew.arvores.exception.RecursoNaoEncontradoException;
import com.greenew.arvores.model.dto.BiomaRequestDTO;
import com.greenew.arvores.model.dto.BiomaResponseDTO;
import com.greenew.arvores.model.entity.BiomaEntity;
import com.greenew.arvores.model.mapper.BiomaMapper;
import com.greenew.arvores.repository.BiomaRepository;
import com.greenew.arvores.service.BiomaService;
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
@DisplayName("Testes para a classe BiomaServiceImpl")
class BiomaServiceImplTest {

    @Mock
    private BiomaRepository biomaRepository;

    @Mock
    private BiomaMapper biomaMapper;

    @InjectMocks
    private BiomaServiceImpl biomaService;

    private BiomaEntity biomaEntity;
    private BiomaRequestDTO biomaRequestDTO; // NOVO: Para entrada
    private BiomaResponseDTO biomaResponseDTO;
    private UUID biomaId;

    @BeforeEach
    void setUp() {
        biomaId = UUID.randomUUID();
        biomaEntity = new BiomaEntity();
        biomaEntity.setId(biomaId);
        biomaEntity.setNome("Cerrado");
        biomaEntity.setDescricao("Bioma brasileiro de savana tropical.");

        biomaRequestDTO = new BiomaRequestDTO("Cerrado", "Bioma brasileiro de savana tropical.");

        biomaResponseDTO = new BiomaResponseDTO();
        biomaResponseDTO.setId(biomaId);
        biomaResponseDTO.setNome("Cerrado");
        biomaResponseDTO.setDescricao("Bioma brasileiro de savana tropical.");
    }

    @Test
    @DisplayName("Deve criar um bioma com sucesso")
    void criar_Success() {
        when(biomaMapper.toEntity(any(BiomaRequestDTO.class))).thenReturn(biomaEntity); // Usa RequestDTO
        when(biomaRepository.save(any(BiomaEntity.class))).thenReturn(biomaEntity);
        when(biomaMapper.toResponseDTO(any(BiomaEntity.class))).thenReturn(biomaResponseDTO); // Usa toResponseDTO

        BiomaResponseDTO resultado = biomaService.criar(biomaRequestDTO); // Usa RequestDTO

        assertThat(resultado).isEqualTo(biomaResponseDTO);
        verify(biomaRepository).save(biomaEntity);
    }

    @Test
    @DisplayName("Deve buscar um bioma por ID com sucesso")
    void buscarPorId_Success() {
        when(biomaRepository.findById(biomaId)).thenReturn(Optional.of(biomaEntity));
        when(biomaMapper.toResponseDTO(any(BiomaEntity.class))).thenReturn(biomaResponseDTO); // Usa toResponseDTO

        BiomaResponseDTO resultado = biomaService.buscarPorId(biomaId); // Retorna ResponseDTO

        assertThat(resultado).isEqualTo(biomaResponseDTO);
    }

    @Test
    @DisplayName("Deve buscar a Entity por ID com sucesso para uso interno")
    void getEntityById_Success() {
        when(biomaRepository.findById(biomaId)).thenReturn(Optional.of(biomaEntity));

        BiomaEntity resultado = biomaService.getEntityById(biomaId); // Chama getEntityById

        assertThat(resultado).isEqualTo(biomaEntity);
    }

    @Test
    @DisplayName("Deve lançar RecursoNaoEncontradoException ao buscar Entity por ID inexistente")
    void getEntityById_NotFound() {
        when(biomaRepository.findById(biomaId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> biomaService.getEntityById(biomaId))
                .isInstanceOf(RecursoNaoEncontradoException.class)
                .hasMessage("Bioma não encontrado com ID: " + biomaId);
    }

    @Test
    @DisplayName("Deve buscar todos os biomas com sucesso")
    void buscarTodos_Success() {
        List<BiomaEntity> biomas = List.of(biomaEntity);
        when(biomaRepository.findAll()).thenReturn(biomas);
        when(biomaMapper.toResponseDTO(any(BiomaEntity.class))).thenReturn(biomaResponseDTO); // Usa toResponseDTO

        List<BiomaResponseDTO> resultado = biomaService.buscarTodos();

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0)).isEqualTo(biomaResponseDTO);
    }

    @Test
    @DisplayName("Deve atualizar um bioma existente com sucesso")
    void atualizar_Success() {
        when(biomaRepository.findById(biomaId)).thenReturn(Optional.of(biomaEntity));

        // Mocking the updateEntityFromDTO logic
        doNothing().when(biomaMapper).updateEntityFromDTO(any(BiomaRequestDTO.class), eq(biomaEntity));

        when(biomaRepository.save(any(BiomaEntity.class))).thenReturn(biomaEntity);
        when(biomaMapper.toResponseDTO(any(BiomaEntity.class))).thenReturn(biomaResponseDTO);

        BiomaResponseDTO resultado = biomaService.atualizar(biomaId, biomaRequestDTO); // Usa RequestDTO

        assertThat(resultado).isEqualTo(biomaResponseDTO);
        verify(biomaRepository).save(biomaEntity);
    }

    @Test
    @DisplayName("Deve lançar RecursoNaoEncontradoException ao tentar atualizar bioma inexistente")
    void atualizar_NotFound() {
        when(biomaRepository.findById(biomaId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> biomaService.atualizar(biomaId, biomaRequestDTO)) // Usa RequestDTO
                .isInstanceOf(RecursoNaoEncontradoException.class)
                .hasMessage("Bioma não encontrado com ID: " + biomaId);
    }

    @Test
    @DisplayName("Deve deletar um bioma com sucesso")
    void deletar_Success() {
        when(biomaRepository.existsById(biomaId)).thenReturn(true);
        doNothing().when(biomaRepository).deleteById(biomaId);

        biomaService.deletar(biomaId);

        verify(biomaRepository).deleteById(biomaId);
    }

    @Test
    @DisplayName("Deve lançar RecursoNaoEncontradoException ao tentar deletar bioma inexistente")
    void deletar_NotFound() {
        when(biomaRepository.existsById(biomaId)).thenReturn(false);

        assertThatThrownBy(() -> biomaService.deletar(biomaId))
                .isInstanceOf(RecursoNaoEncontradoException.class)
                .hasMessage("Bioma não encontrado com ID: " + biomaId);
        verify(biomaRepository, never()).deleteById(any(UUID.class));
    }
}