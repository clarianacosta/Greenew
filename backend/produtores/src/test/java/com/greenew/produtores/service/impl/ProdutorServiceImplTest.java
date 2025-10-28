package com.greenew.produtores.service.impl;

import com.greenew.produtores.exception.RecursoNaoEncontradoException;
import com.greenew.produtores.model.dto.ProdutorRequestDTO;
import com.greenew.produtores.model.dto.ProdutorResponseDTO;
import com.greenew.produtores.model.dto.TerrenoResponseDTO;
import com.greenew.produtores.model.entity.ProdutorEntity;
import com.greenew.produtores.model.entity.TerrenoEntity;
import com.greenew.produtores.model.mapper.ProdutorMapper;
import com.greenew.produtores.repository.ProdutorRepository;
import com.greenew.produtores.service.TerrenoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("Testes para a classe ProdutorServiceImpl")
class ProdutorServiceImplTest {

    @Mock
    private ProdutorRepository produtorRepository;

    @Mock
    private ProdutorMapper produtorMapper;

    @Mock
    private TerrenoService terrenoService;

    @InjectMocks
    private ProdutorServiceImpl produtorService;

    private ProdutorEntity produtorEntity;
    private ProdutorRequestDTO produtorRequestDTO;
    private ProdutorResponseDTO produtorResponseDTO;
    private UUID produtorId;
    private final String email = "joao.silva@teste.com";

    @BeforeEach
    void setUp() {
        produtorId = UUID.randomUUID();

        produtorEntity = new ProdutorEntity();
        produtorEntity.setId(produtorId);
        produtorEntity.setNomeCompleto("João da Silva");
        produtorEntity.setEmail(email);
        produtorEntity.setCelular("11987654321");
        produtorEntity.setTerrenos(Collections.emptySet());

        produtorRequestDTO = new ProdutorRequestDTO("João da Silva", email, "11987654321");

        produtorResponseDTO = new ProdutorResponseDTO();
        produtorResponseDTO.setId(produtorId);
        produtorResponseDTO.setEmail(email);
        produtorResponseDTO.setNomeCompleto("João da Silva");
        produtorResponseDTO.setTerrenos(Collections.emptySet());
    }

    @Test
    @DisplayName("Deve criar um produtor com sucesso")
    void criar_Success() {
        when(produtorRepository.existsByEmail(email)).thenReturn(false);
        when(produtorMapper.toEntity(any(ProdutorRequestDTO.class))).thenReturn(produtorEntity);
        when(produtorRepository.save(any(ProdutorEntity.class))).thenReturn(produtorEntity);
        when(produtorMapper.toResponseDTO(any(ProdutorEntity.class))).thenReturn(produtorResponseDTO);

        ProdutorResponseDTO resultado = produtorService.criar(produtorRequestDTO);

        assertThat(resultado).isEqualTo(produtorResponseDTO);
        verify(produtorRepository).existsByEmail(email);
        verify(produtorRepository).save(produtorEntity);
    }

    @Test
    @DisplayName("Deve lançar IllegalArgumentException ao tentar criar produtor com e-mail duplicado")
    void criar_EmailDuplicado() {
        when(produtorRepository.existsByEmail(email)).thenReturn(true);

        assertThatThrownBy(() -> produtorService.criar(produtorRequestDTO))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Já existe um produtor com este e-mail.");

        verify(produtorRepository).existsByEmail(email);
        verify(produtorRepository, never()).save(any(ProdutorEntity.class));
    }

    @Test
    @DisplayName("Deve buscar um produtor por ID com sucesso")
    void buscarPorId_Success() {
        when(produtorRepository.findById(produtorId)).thenReturn(Optional.of(produtorEntity));
        when(produtorMapper.toResponseDTO(produtorEntity)).thenReturn(produtorResponseDTO);

        ProdutorResponseDTO resultado = produtorService.buscarPorId(produtorId);

        assertThat(resultado).isEqualTo(produtorResponseDTO);
        verify(produtorRepository).findById(produtorId);
    }

    @Test
    @DisplayName("Deve lançar RecursoNaoEncontradoException ao buscar por ID inexistente")
    void buscarPorId_NotFound() {
        when(produtorRepository.findById(produtorId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> produtorService.buscarPorId(produtorId))
                .isInstanceOf(RecursoNaoEncontradoException.class)
                .hasMessage("Produtor não encontrado com ID: " + produtorId);
    }

    @Test
    @DisplayName("Deve buscar todos os produtores com sucesso")
    void buscarTodos_Success() {
        List<ProdutorEntity> produtores = List.of(produtorEntity);
        when(produtorRepository.findAll()).thenReturn(produtores);
        when(produtorMapper.toResponseDTO(produtorEntity)).thenReturn(produtorResponseDTO);

        List<ProdutorResponseDTO> resultado = produtorService.buscarTodos();

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0)).isEqualTo(produtorResponseDTO);
    }

    @Test
    @DisplayName("Deve atualizar um produtor existente com sucesso")
    void atualizar_Success() {
        ProdutorRequestDTO newRequest = new ProdutorRequestDTO("Novo Nome", "novo.email@teste.com", "22998877665");

        when(produtorRepository.findById(produtorId)).thenReturn(Optional.of(produtorEntity));
        when(produtorRepository.existsByEmail("novo.email@teste.com")).thenReturn(false);
        doNothing().when(produtorMapper).updateEntityFromDTO(any(ProdutorRequestDTO.class), any(ProdutorEntity.class));
        when(produtorRepository.save(any(ProdutorEntity.class))).thenReturn(produtorEntity);
        when(produtorMapper.toResponseDTO(any(ProdutorEntity.class))).thenReturn(produtorResponseDTO);

        ProdutorResponseDTO resultado = produtorService.atualizar(produtorId, newRequest);

        assertThat(resultado).isEqualTo(produtorResponseDTO);
        verify(produtorMapper).updateEntityFromDTO(eq(newRequest), eq(produtorEntity));
        verify(produtorRepository).save(produtorEntity);
    }


    @Test
    @DisplayName("Deve atualizar um produtor com sucesso quando o e-mail não é alterado")
    void atualizar_Success_EmailNotChanged() {
        ProdutorRequestDTO sameEmailRequest = new ProdutorRequestDTO("Novo Nome", email, "22998877665");

        when(produtorRepository.findById(produtorId)).thenReturn(Optional.of(produtorEntity));
        doNothing().when(produtorMapper).updateEntityFromDTO(any(ProdutorRequestDTO.class), any(ProdutorEntity.class));
        when(produtorRepository.save(any(ProdutorEntity.class))).thenReturn(produtorEntity);
        when(produtorMapper.toResponseDTO(any(ProdutorEntity.class))).thenReturn(produtorResponseDTO);

        ProdutorResponseDTO resultado = produtorService.atualizar(produtorId, sameEmailRequest);

        assertThat(resultado).isEqualTo(produtorResponseDTO);
        verify(produtorRepository, never()).existsByEmail(anyString());
        verify(produtorRepository).save(produtorEntity);
    }

    @Test
    @DisplayName("Deve lançar RecursoNaoEncontradoException ao tentar atualizar produtor inexistente")
    void atualizar_NotFound() {
        when(produtorRepository.findById(produtorId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> produtorService.atualizar(produtorId, produtorRequestDTO))
                .isInstanceOf(RecursoNaoEncontradoException.class)
                .hasMessage("Produtor não encontrado com ID: " + produtorId);
    }

    @Test
    @DisplayName("Deve lançar IllegalArgumentException ao tentar atualizar produtor com e-mail já em uso")
    void atualizar_EmailEmUso() {
        String emailEmUso = "outro@email.com";
        ProdutorRequestDTO newRequest = new ProdutorRequestDTO("Novo Nome", emailEmUso, "22998877665");
        produtorEntity.setEmail(email);

        when(produtorRepository.findById(produtorId)).thenReturn(Optional.of(produtorEntity));
        when(produtorRepository.existsByEmail(emailEmUso)).thenReturn(true);

        assertThatThrownBy(() -> produtorService.atualizar(produtorId, newRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("O e-mail fornecido já está em uso por outro produtor.");
        verify(produtorRepository, never()).save(any(ProdutorEntity.class));
    }

    @Test
    @DisplayName("Deve deletar um produtor com sucesso")
    void deletar_Success() {
        when(produtorRepository.existsById(produtorId)).thenReturn(true);
        doNothing().when(produtorRepository).deleteById(produtorId);

        produtorService.deletar(produtorId);

        verify(produtorRepository).deleteById(produtorId);
    }

    @Test
    @DisplayName("Deve lançar RecursoNaoEncontradoException ao tentar deletar produtor inexistente")
    void deletar_NotFound() {
        when(produtorRepository.existsById(produtorId)).thenReturn(false);

        assertThatThrownBy(() -> produtorService.deletar(produtorId))
                .isInstanceOf(RecursoNaoEncontradoException.class)
                .hasMessage("Produtor não encontrado com ID: " + produtorId);
        verify(produtorRepository, never()).deleteById(any(UUID.class));
    }
}