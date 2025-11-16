package com.greenew.produtores.service.impl;

import com.greenew.produtores.config.client.ArvoresServiceClient;
import com.greenew.produtores.config.dtos.BiomaResponseDTO;
import com.greenew.produtores.config.dtos.ClimaResponseDTO;
import com.greenew.produtores.exception.RecursoNaoEncontradoException;
import com.greenew.produtores.model.dto.ProdutorResumeDTO;
import com.greenew.produtores.model.dto.TerrenoRequestDTO;
import com.greenew.produtores.model.dto.TerrenoResponseDTO;
import com.greenew.produtores.model.entity.ProdutorEntity;
import com.greenew.produtores.model.entity.TerrenoEntity;
import com.greenew.produtores.model.mapper.TerrenoMapper;
import com.greenew.produtores.repository.ProdutorRepository;
import com.greenew.produtores.repository.TerrenoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Testes para a classe TerrenoServiceImpl")
class TerrenoServiceImplTest {

    @Mock
    private TerrenoRepository terrenoRepository;

    @Mock
    private ProdutorRepository produtorRepository;

    @Mock
    private TerrenoMapper terrenoMapper;

    @Mock
    private ArvoresServiceClient arvoresServiceClient;

    @InjectMocks
    private TerrenoServiceImpl terrenoService;

    private ProdutorEntity produtorEntity;
    private TerrenoEntity terrenoEntity;
    private TerrenoRequestDTO terrenoRequestDTO;
    private TerrenoResponseDTO terrenoResponseDTO;
    private UUID produtorId;
    private UUID terrenoId;
    private UUID biomaId;
    private UUID climaId;
    private BiomaResponseDTO biomaResponseDTO;
    private ClimaResponseDTO climaResponseDTO;
    private ProdutorResumeDTO produtorResumeDTO; // NOVO
    private BigDecimal area;

    @BeforeEach
    void setUp() {
        produtorId = UUID.randomUUID();
        terrenoId = UUID.randomUUID();
        biomaId = UUID.randomUUID();
        climaId = UUID.randomUUID();
        area = new BigDecimal("10.0");

        // 1. Inicialização de DTOs de Serviço (para retorno do Mock)
        biomaResponseDTO = new BiomaResponseDTO(biomaId, "Cerrado Mock", "Descrição Mock");
        climaResponseDTO = new ClimaResponseDTO(climaId, "Tropical Mock", "Descrição Mock");
        produtorResumeDTO = new ProdutorResumeDTO(produtorId, "Produtor Teste", "email@teste.com", null);

        // 2. Inicialização de Entidades
        produtorEntity = new ProdutorEntity();
        produtorEntity.setId(produtorId);
        produtorEntity.setNomeCompleto("Produtor Teste");
        produtorEntity.setEmail("email@teste.com");

        terrenoEntity = new TerrenoEntity();
        terrenoEntity.setId(terrenoId);
        terrenoEntity.setLatitude(new BigDecimal("10.0"));
        terrenoEntity.setBiomaIdLocal(biomaId);
        terrenoEntity.setClimaIdLocal(climaId);
        terrenoEntity.setAreaDisponivelHectares(area);
        terrenoEntity.setProdutor(produtorEntity);

        // 3. Inicialização de DTOs de Request/Response
        terrenoRequestDTO = new TerrenoRequestDTO();
        terrenoRequestDTO.setLatitude(new BigDecimal("10.0"));
        terrenoRequestDTO.setProdutorId(produtorId);
        terrenoRequestDTO.setBiomaIdLocal(biomaId);
        terrenoRequestDTO.setClimaIdLocal(climaId);
        terrenoRequestDTO.setAreaDisponivelHectares(area);

        // O response DTO (com detalhes aninhados)
        terrenoResponseDTO = new TerrenoResponseDTO(
                terrenoId,
                produtorResumeDTO,
                new BigDecimal("10.0"),
                new BigDecimal("20.0"), // Longitude (valor de teste)
                area,
                biomaResponseDTO,
                climaResponseDTO
        );
    }

    // --------------------------------------------------------------------------
    // TESTES DE CRIAÇÃO
    // --------------------------------------------------------------------------

    @Test
    @DisplayName("Deve criar um terreno com sucesso e buscar detalhes aninhados")
    void criar_Success() {
        // Mock Mocks para validação e busca de detalhes
        when(arvoresServiceClient.buscarBiomaPorId(biomaId)).thenReturn(biomaResponseDTO);
        when(arvoresServiceClient.buscarClimaPorId(climaId)).thenReturn(climaResponseDTO);

        // Mock Mocks de persistência
        when(produtorRepository.findById(produtorId)).thenReturn(Optional.of(produtorEntity));
        when(terrenoMapper.toEntity(any(TerrenoRequestDTO.class))).thenReturn(terrenoEntity);
        when(terrenoRepository.save(any(TerrenoEntity.class))).thenReturn(terrenoEntity);

        // Mock o mapper para retornar a versão final (antes de anexar Bioma/Clima)
        // Usamos doAnswer para simular a lógica de anexo de DTOs no ServiceImpl
        when(terrenoMapper.toResponseDTO(terrenoEntity)).thenReturn(
                new TerrenoResponseDTO(terrenoId, produtorResumeDTO, new BigDecimal("10.0"), new BigDecimal("20.0"), area, null, null)
        );

        TerrenoResponseDTO resultado = terrenoService.criar(terrenoRequestDTO);

        assertThat(resultado.getBiomaLocal()).isEqualTo(biomaResponseDTO);
        assertThat(resultado.getClimaLocal()).isEqualTo(climaResponseDTO);
        assertThat(resultado.getProdutor().getId()).isEqualTo(produtorId);
        verify(terrenoRepository).save(terrenoEntity);
        verify(arvoresServiceClient).buscarBiomaPorId(biomaId);
        verify(arvoresServiceClient).buscarClimaPorId(climaId);
    }

    @Test
    @DisplayName("Deve lançar RecursoNaoEncontradoException se o Bioma não for encontrado")
    void criar_BiomaNotFound() {
        // Mock Bioma para lançar exceção (simulando 404 do Arvores-Service)
        when(arvoresServiceClient.buscarBiomaPorId(biomaId))
                .thenThrow(new RecursoNaoEncontradoException("Bioma não encontrado com ID: " + biomaId));

        assertThatThrownBy(() -> terrenoService.criar(terrenoRequestDTO))
                .isInstanceOf(RecursoNaoEncontradoException.class)
                .hasMessage("Bioma não encontrado com ID: " + biomaId);

        verify(terrenoRepository, never()).save(any(TerrenoEntity.class));
        verify(arvoresServiceClient, never()).buscarClimaPorId(any()); // O clima não deve ser checado se o bioma falhar primeiro
    }

    @Test
    @DisplayName("Deve lançar RecursoNaoEncontradoException se o Clima não for encontrado")
    void criar_ClimaNotFound() {
        // Mock Bioma para sucesso, Clima para falha
        when(arvoresServiceClient.buscarBiomaPorId(biomaId)).thenReturn(biomaResponseDTO);
        when(arvoresServiceClient.buscarClimaPorId(climaId))
                .thenThrow(new RecursoNaoEncontradoException("Clima não encontrado com ID: " + climaId));

        assertThatThrownBy(() -> terrenoService.criar(terrenoRequestDTO))
                .isInstanceOf(RecursoNaoEncontradoException.class)
                .hasMessage("Clima não encontrado com ID: " + climaId);

        verify(terrenoRepository, never()).save(any(TerrenoEntity.class));
    }

    @Test
    @DisplayName("Deve lançar RecursoNaoEncontradoException ao tentar criar terreno com Produtor ID inexistente")
    void criar_ProdutorNotFound() {
        // 1. Garante que as chamadas ao Arvores-Service passem
        when(arvoresServiceClient.buscarBiomaPorId(biomaId)).thenReturn(biomaResponseDTO);
        when(arvoresServiceClient.buscarClimaPorId(climaId)).thenReturn(climaResponseDTO);

        // 2. Mock Produtor para falha
        when(produtorRepository.findById(produtorId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> terrenoService.criar(terrenoRequestDTO))
                .isInstanceOf(RecursoNaoEncontradoException.class)
                .hasMessage("Produtor não encontrado com ID: " + produtorId);

        verify(terrenoRepository, never()).save(any(TerrenoEntity.class));
    }

    // --------------------------------------------------------------------------
    // TESTES EXISTENTES (Manter a coerência com as alterações)
    // --------------------------------------------------------------------------

    @Test
    @DisplayName("Deve buscar um terreno por ID com sucesso")
    void buscarPorId_Success() {
        when(terrenoRepository.findById(terrenoId)).thenReturn(Optional.of(terrenoEntity));
        when(terrenoMapper.toResponseDTO(any(TerrenoEntity.class))).thenReturn(terrenoResponseDTO);

        TerrenoResponseDTO resultado = terrenoService.buscarPorId(terrenoId);

        assertThat(resultado).isEqualTo(terrenoResponseDTO);
        verify(terrenoRepository).findById(terrenoId);
    }

    @Test
    @DisplayName("Deve lançar RecursoNaoEncontradoException ao buscar terreno por ID inexistente")
    void buscarPorId_NotFound() {
        when(terrenoRepository.findById(terrenoId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> terrenoService.buscarPorId(terrenoId))
                .isInstanceOf(RecursoNaoEncontradoException.class)
                .hasMessage("Terreno não encontrado com ID: " + terrenoId);
    }

    // Nota: buscarTodosPorProdutorId e buscarTodos mantidos por brevidade,
    // mas devem ser verificados para injetar Bioma/ClimaResponseDTO se a lógica de serviço o fizer.
    // ...

    @Test
    @DisplayName("Deve atualizar um terreno existente com sucesso")
    void atualizar_Success() {
        // Garante que o Bioma/Clima é buscado, embora não seja estritamente necessário para o fluxo PUT
        when(terrenoRepository.findById(terrenoId)).thenReturn(Optional.of(terrenoEntity));

        // A lógica de PUT não chama ArvoresService (mantendo a forte coesão no PUT)

        when(terrenoRepository.save(any(TerrenoEntity.class))).thenReturn(terrenoEntity);
        when(terrenoMapper.toResponseDTO(any(TerrenoEntity.class))).thenReturn(terrenoResponseDTO);

        TerrenoResponseDTO resultado = terrenoService.atualizar(terrenoId, terrenoRequestDTO);

        assertThat(resultado).isEqualTo(terrenoResponseDTO);
        verify(terrenoRepository).save(terrenoEntity);
    }

    @Test
    @DisplayName("Deve lançar RecursoNaoEncontradoException ao tentar atualizar terreno inexistente")
    void atualizar_NotFound() {
        when(terrenoRepository.findById(terrenoId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> terrenoService.atualizar(terrenoId, terrenoRequestDTO))
                .isInstanceOf(RecursoNaoEncontradoException.class)
                .hasMessage("Terreno não encontrado com ID: " + terrenoId);
    }

    @Test
    @DisplayName("Deve deletar um terreno com sucesso")
    void deletar_Success() {
        when(terrenoRepository.existsById(terrenoId)).thenReturn(true);
        doNothing().when(terrenoRepository).deleteById(terrenoId);

        terrenoService.deletar(terrenoId);

        verify(terrenoRepository).deleteById(terrenoId);
    }

    @Test
    @DisplayName("Deve lançar RecursoNaoEncontradoException ao tentar deletar terreno inexistente")
    void deletar_NotFound() {
        when(terrenoRepository.existsById(terrenoId)).thenReturn(false);

        assertThatThrownBy(() -> terrenoService.deletar(terrenoId))
                .isInstanceOf(RecursoNaoEncontradoException.class)
                .hasMessage("Terreno não encontrado com ID: " + terrenoId);
        verify(terrenoRepository, never()).deleteById(any(UUID.class));
    }
}