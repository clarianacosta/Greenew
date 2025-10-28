package com.greenew.arvores.service.impl;
import java.util.UUID;
import java.util.UUID;

import com.greenew.arvores.exception.RecursoNaoEncontradoException;
import com.greenew.arvores.model.dto.ArvoreRequestDTO;
import com.greenew.arvores.model.dto.ArvoreResponseDTO;
import com.greenew.arvores.model.entity.*;
import com.greenew.arvores.model.mapper.ArvoreMapper;
import com.greenew.arvores.repository.ArvoreRepository;
import com.greenew.arvores.service.BiomaService;
import com.greenew.arvores.service.ClimaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.ArgumentMatchers.any;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes para a classe ArvoreServiceImpl")
class ArvoreServiceImplTest {

    @Mock
    private ArvoreRepository arvoreRepository;

    @Mock
    private BiomaService biomaService;

    @Mock
    private ClimaService climaService;

    @Mock
    private ArvoreMapper arvoreMapper;

    @InjectMocks
    private ArvoreServiceImpl arvoreService;

    private ArvoreEntity arvoreEntity;
    private ArvoreRequestDTO arvoreRequestDTO;
    private ArvoreResponseDTO arvoreResponseDTO;
    private UUID arvoreId;
    private UUID biomaId;
    private UUID climaId;

    @BeforeEach
    void setUp() {
        arvoreId = UUID.randomUUID();
        biomaId = UUID.randomUUID();
        climaId = UUID.randomUUID();

        arvoreEntity = new ArvoreEntity();
        arvoreEntity.setId(arvoreId);
        arvoreEntity.setNomePopular("Ipê Amarelo");
        arvoreEntity.setNomeCientifico("Handroanthus albus");
        arvoreEntity.setTaxaAbsorcaoCo2Anual(new BigDecimal("22.5000"));

        arvoreRequestDTO = new ArvoreRequestDTO();
        arvoreRequestDTO.setNomePopular("Ipê Amarelo");
        arvoreRequestDTO.setNomeCientifico("Handroanthus albus");
        arvoreRequestDTO.setTaxaAbsorcaoCo2Anual(new BigDecimal("22.5000"));

        arvoreResponseDTO = new ArvoreResponseDTO();
        arvoreResponseDTO.setId(arvoreId);
        arvoreResponseDTO.setNomePopular("Ipê Amarelo");
        arvoreResponseDTO.setNomeCientifico("Handroanthus albus");
        arvoreResponseDTO.setTaxaAbsorcaoCo2Anual(new BigDecimal("22.5000"));
    }

    @Test
    @DisplayName("Deve criar uma árvore com sucesso e associar biomas e climas")
    void criar_Success() {
        Set<UUID> biomasIds = Set.of(biomaId);
        Set<UUID> climasIds = Set.of(climaId);
        arvoreRequestDTO.setBiomasIds(biomasIds);
        arvoreRequestDTO.setClimasIds(climasIds);

        BiomaEntity biomaMock = new BiomaEntity();
        biomaMock.setId(biomaId);
        ClimaEntity climaMock = new ClimaEntity();
        climaMock.setId(climaId);

        when(arvoreMapper.toEntity(any(ArvoreRequestDTO.class))).thenReturn(arvoreEntity);
        // CORREÇÃO: Usando getEntityById para retornar a Entity diretamente (necessário para a lógica de associação)
        when(biomaService.getEntityById(biomaId)).thenReturn(biomaMock);
        when(climaService.getEntityById(climaId)).thenReturn(climaMock);
        when(arvoreRepository.save(any(ArvoreEntity.class))).thenReturn(arvoreEntity);
        when(arvoreMapper.toResponseDTO(any(ArvoreEntity.class))).thenReturn(arvoreResponseDTO);

        ArvoreResponseDTO resultado = arvoreService.criar(arvoreRequestDTO);

        assertThat(resultado).isEqualTo(arvoreResponseDTO);
        assertThat(arvoreEntity.getBiomasAssociados()).hasSize(1);
        assertThat(arvoreEntity.getClimasAssociados()).hasSize(1);
        verify(arvoreRepository).save(arvoreEntity);
    }

    @Test
    @DisplayName("Deve lançar RecursoNaoEncontradoException ao criar com bioma inexistente")
    void criar_BiomaNotFound() {
        Set<UUID> biomasIds = Set.of(biomaId);
        arvoreRequestDTO.setBiomasIds(biomasIds);

        when(arvoreMapper.toEntity(any(ArvoreRequestDTO.class))).thenReturn(arvoreEntity);
        // CORREÇÃO: Mockando getEntityById para lançar exceção
        when(biomaService.getEntityById(biomaId)).thenThrow(new RecursoNaoEncontradoException("Bioma não encontrado com ID: " + biomaId));

        assertThatThrownBy(() -> arvoreService.criar(arvoreRequestDTO))
                .isInstanceOf(RecursoNaoEncontradoException.class)
                .hasMessage("Bioma não encontrado com ID: " + biomaId);
    }

    @Test
    @DisplayName("Deve lançar RecursoNaoEncontradoException ao criar com clima inexistente")
    void criar_ClimaNotFound() {
        Set<UUID> biomasIds = Set.of(biomaId);
        Set<UUID> climasIds = Set.of(climaId);
        arvoreRequestDTO.setBiomasIds(biomasIds);
        arvoreRequestDTO.setClimasIds(climasIds);

        BiomaEntity biomaMock = new BiomaEntity();
        biomaMock.setId(biomaId);

        when(arvoreMapper.toEntity(any(ArvoreRequestDTO.class))).thenReturn(arvoreEntity);
        // Mock Bioma (Sucesso)
        when(biomaService.getEntityById(biomaId)).thenReturn(biomaMock);
        // Mock Clima (Falha)
        when(climaService.getEntityById(climaId)).thenThrow(new RecursoNaoEncontradoException("Clima não encontrado com ID: " + climaId));

        assertThatThrownBy(() -> arvoreService.criar(arvoreRequestDTO))
                .isInstanceOf(RecursoNaoEncontradoException.class)
                .hasMessage("Clima não encontrado com ID: " + climaId);
    }

    @Test
    @DisplayName("Deve buscar uma árvore por ID com sucesso")
    void buscarPorId_Success() {
        when(arvoreRepository.findById(arvoreId)).thenReturn(Optional.of(arvoreEntity));
        when(arvoreMapper.toResponseDTO(any(ArvoreEntity.class))).thenReturn(arvoreResponseDTO);

        ArvoreResponseDTO resultado = arvoreService.buscarPorId(arvoreId);

        assertThat(resultado).isEqualTo(arvoreResponseDTO);
        verify(arvoreRepository).findById(arvoreId);
    }

    @Test
    @DisplayName("Deve lançar RecursoNaoEncontradoException ao buscar por ID inexistente")
    void buscarPorId_NotFound() {
        when(arvoreRepository.findById(arvoreId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> arvoreService.buscarPorId(arvoreId))
                .isInstanceOf(RecursoNaoEncontradoException.class)
                .hasMessage("Árvore não encontrada com ID: " + arvoreId);
    }

    @Test
    @DisplayName("Deve buscar todas as árvores com sucesso")
    void buscarTodas_Success() {
        List<ArvoreEntity> arvores = List.of(arvoreEntity);
        when(arvoreRepository.findAll()).thenReturn(arvores);
        when(arvoreMapper.toResponseDTO(any(ArvoreEntity.class))).thenReturn(arvoreResponseDTO);

        List<ArvoreResponseDTO> resultado = arvoreService.buscarTodas();

        assertThat(resultado).hasSize(1);
        assertThat(resultado.get(0)).isEqualTo(arvoreResponseDTO);
    }

    @Test
    @DisplayName("Deve atualizar uma árvore existente com sucesso")
    void atualizar_Success() {
        Set<UUID> biomasIds = Set.of(biomaId);
        Set<UUID> climasIds = Set.of(climaId);
        arvoreRequestDTO.setBiomasIds(biomasIds);
        arvoreRequestDTO.setClimasIds(climasIds);

        BiomaEntity biomaMock = new BiomaEntity();
        biomaMock.setId(biomaId);
        ClimaEntity climaMock = new ClimaEntity();
        climaMock.setId(climaId);

        when(arvoreRepository.findById(arvoreId)).thenReturn(Optional.of(arvoreEntity));
        // CORREÇÃO: Usando getEntityById
        when(biomaService.getEntityById(biomaId)).thenReturn(biomaMock);
        when(climaService.getEntityById(climaId)).thenReturn(climaMock);
        when(arvoreRepository.save(any(ArvoreEntity.class))).thenReturn(arvoreEntity);
        when(arvoreMapper.toResponseDTO(any(ArvoreEntity.class))).thenReturn(arvoreResponseDTO);

        ArvoreResponseDTO resultado = arvoreService.atualizar(arvoreId, arvoreRequestDTO);

        assertThat(resultado).isEqualTo(arvoreResponseDTO);
        assertThat(arvoreEntity.getBiomasAssociados()).hasSize(1);
        assertThat(arvoreEntity.getClimasAssociados()).hasSize(1);
        verify(arvoreRepository).save(arvoreEntity);
    }

    @Test
    @DisplayName("Deve lançar RecursoNaoEncontradoException ao tentar atualizar árvore inexistente")
    void atualizar_NotFound() {
        when(arvoreRepository.findById(arvoreId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> arvoreService.atualizar(arvoreId, arvoreRequestDTO))
                .isInstanceOf(RecursoNaoEncontradoException.class)
                .hasMessage("Árvore não encontrada com ID: " + arvoreId);
    }

    @Test
    @DisplayName("Deve atualizar uma árvore, limpando biomas e climas existentes")
    void atualizar_Success_ClearsExistingAssociations() {
        Set<UUID> biomasIds = Set.of(biomaId);
        Set<UUID> climasIds = Set.of(climaId);
        arvoreRequestDTO.setBiomasIds(biomasIds);
        arvoreRequestDTO.setClimasIds(climasIds);

        BiomaEntity biomaMock = new BiomaEntity();
        biomaMock.setId(biomaId);
        ClimaEntity climaMock = new ClimaEntity();
        climaMock.setId(climaId);

        arvoreEntity.setBiomasAssociados(new HashSet<>());
        arvoreEntity.setClimasAssociados(new HashSet<>());

        when(arvoreRepository.findById(arvoreId)).thenReturn(Optional.of(arvoreEntity));
        // CORREÇÃO: Usando getEntityById
        when(biomaService.getEntityById(biomaId)).thenReturn(biomaMock);
        when(climaService.getEntityById(climaId)).thenReturn(climaMock);
        when(arvoreRepository.save(any(ArvoreEntity.class))).thenReturn(arvoreEntity);
        when(arvoreMapper.toResponseDTO(any(ArvoreEntity.class))).thenReturn(arvoreResponseDTO);

        ArvoreResponseDTO resultado = arvoreService.atualizar(arvoreId, arvoreRequestDTO);

        assertThat(resultado).isEqualTo(arvoreResponseDTO);
        assertThat(arvoreEntity.getBiomasAssociados()).hasSize(1);
        assertThat(arvoreEntity.getClimasAssociados()).hasSize(1);
        verify(arvoreRepository).save(arvoreEntity);
    }

    @Test
    @DisplayName("Deve criar uma árvore, garantindo que a limpeza não falhe com associações nulas")
    void criar_Success_WithNullAssociations() {
        Set<UUID> biomasIds = Set.of(biomaId);
        Set<UUID> climasIds = Set.of(climaId);
        arvoreRequestDTO.setBiomasIds(biomasIds);
        arvoreRequestDTO.setClimasIds(climasIds);

        BiomaEntity biomaMock = new BiomaEntity();
        biomaMock.setId(biomaId);
        ClimaEntity climaMock = new ClimaEntity();
        climaMock.setId(climaId);

        arvoreEntity.setBiomasAssociados(null);
        arvoreEntity.setClimasAssociados(null);

        when(arvoreMapper.toEntity(any(ArvoreRequestDTO.class))).thenReturn(arvoreEntity);
        // CORREÇÃO: Usando getEntityById
        when(biomaService.getEntityById(biomaId)).thenReturn(biomaMock);
        when(climaService.getEntityById(climaId)).thenReturn(climaMock);
        when(arvoreRepository.save(any(ArvoreEntity.class))).thenReturn(arvoreEntity);
        when(arvoreMapper.toResponseDTO(any(ArvoreEntity.class))).thenReturn(arvoreResponseDTO);

        ArvoreResponseDTO resultado = arvoreService.criar(arvoreRequestDTO);

        assertThat(resultado).isEqualTo(arvoreResponseDTO);
        assertThat(arvoreEntity.getBiomasAssociados()).hasSize(1);
        assertThat(arvoreEntity.getClimasAssociados()).hasSize(1);
        verify(arvoreRepository).save(arvoreEntity);
    }

    @Test
    @DisplayName("Deve deletar uma árvore com sucesso")
    void deletar_Success() {
        when(arvoreRepository.existsById(arvoreId)).thenReturn(true);
        doNothing().when(arvoreRepository).deleteById(arvoreId);

        arvoreService.deletar(arvoreId);

        verify(arvoreRepository).deleteById(arvoreId);
    }

    @Test
    @DisplayName("Deve lançar RecursoNaoEncontradoException ao tentar deletar árvore inexistente")
    void deletar_NotFound() {
        when(arvoreRepository.existsById(arvoreId)).thenReturn(false);

        assertThatThrownBy(() -> arvoreService.deletar(arvoreId))
                .isInstanceOf(RecursoNaoEncontradoException.class)
                .hasMessage("Árvore não encontrada com ID: " + arvoreId);
        verify(arvoreRepository, never()).deleteById(any(UUID.class));
    }
}