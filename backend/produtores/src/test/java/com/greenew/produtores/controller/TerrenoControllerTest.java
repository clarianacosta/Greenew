package com.greenew.produtores.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.greenew.produtores.config.dtos.BiomaResponseDTO;
import com.greenew.produtores.config.dtos.ClimaResponseDTO;
import com.greenew.produtores.exception.RecursoNaoEncontradoException;
import com.greenew.produtores.model.dto.TerrenoRequestDTO;
import com.greenew.produtores.model.dto.TerrenoResponseDTO;
import com.greenew.produtores.service.TerrenoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TerrenoController.class)
@DisplayName("Testes para a classe TerrenoController")
class TerrenoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private TerrenoService terrenoService;

    private TerrenoRequestDTO terrenoRequestDTO;
    private TerrenoResponseDTO terrenoResponseDTO;
    private UUID terrenoId;
    private UUID produtorId;
    private UUID biomaId;
    private UUID climaId;
    private BiomaResponseDTO biomaResponseDTO;
    private ClimaResponseDTO climaResponseDTO;

    @BeforeEach
    void setUp() {
        terrenoId = UUID.randomUUID();
        produtorId = UUID.randomUUID();
        biomaId = UUID.randomUUID();
        climaId = UUID.randomUUID();

        // 1. Inicializa os objetos Bioma e Clima DTO (replicando a estrutura do Arvores-Service)
        biomaResponseDTO = new BiomaResponseDTO(biomaId, "Cerrado Teste", "Descrição Teste");
        climaResponseDTO = new ClimaResponseDTO(climaId, "Tropical Teste", "Descrição Teste");

        // 2. Inicializa Request DTO (usa IDs brutos, o que está correto)
        terrenoRequestDTO = new TerrenoRequestDTO();
        terrenoRequestDTO.setLatitude(new BigDecimal("-15.7801"));
        terrenoRequestDTO.setLongitude(new BigDecimal("-47.9292"));
        terrenoRequestDTO.setBiomaIdLocal(biomaId);
        terrenoRequestDTO.setClimaIdLocal(climaId);
        terrenoRequestDTO.setProdutorId(produtorId);

        // 3. Inicializa Response DTO com os objetos DTOs (corrigindo o erro de tipos)
        // A ordem do construtor deve ser: id, latitude, longitude, BiomaResponseDTO, ClimaResponseDTO, produtorId
        terrenoResponseDTO = new TerrenoResponseDTO(
                terrenoId,
                produtorId,
                new BigDecimal("-15.7801"),
                new BigDecimal("-47.9292"),
                biomaResponseDTO,
                climaResponseDTO
        );
    }

    @Test
    @DisplayName("Deve criar um novo terreno e retornar status 201 Created")
    void criarTerreno_Success() throws Exception {
        when(terrenoService.criar(any(TerrenoRequestDTO.class))).thenReturn(terrenoResponseDTO);

        mockMvc.perform(post("/api/terrenos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(terrenoRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(terrenoId.toString()));
    }

    @Test
    @DisplayName("Deve buscar todos os terrenos e retornar status 200 OK")
    void buscarTodosTerrenos_Success() throws Exception {
        when(terrenoService.buscarTodos()).thenReturn(List.of(terrenoResponseDTO));

        mockMvc.perform(get("/api/terrenos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(terrenoId.toString()));
    }

    @Test
    @DisplayName("Deve buscar terreno por ID e retornar status 200 OK")
    void buscarTerrenoPorId_Success() throws Exception {
        when(terrenoService.buscarPorId(terrenoId)).thenReturn(terrenoResponseDTO);

        mockMvc.perform(get("/api/terrenos/" + terrenoId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(terrenoId.toString()));
    }

    @Test
    @DisplayName("Deve retornar status 404 Not Found ao buscar terreno inexistente")
    void buscarTerrenoPorId_NotFound() throws Exception {
        when(terrenoService.buscarPorId(terrenoId)).thenThrow(new RecursoNaoEncontradoException("Terreno não encontrado."));

        mockMvc.perform(get("/api/terrenos/" + terrenoId))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve atualizar um terreno e retornar status 200 OK")
    void atualizarTerreno_Success() throws Exception {
        when(terrenoService.atualizar(eq(terrenoId), any(TerrenoRequestDTO.class))).thenReturn(terrenoResponseDTO);

        mockMvc.perform(put("/api/terrenos/" + terrenoId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(terrenoRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(terrenoId.toString()));
    }

    @Test
    @DisplayName("Deve retornar status 404 Not Found ao tentar atualizar terreno inexistente")
    void atualizarTerreno_NotFound() throws Exception {
        when(terrenoService.atualizar(eq(terrenoId), any(TerrenoRequestDTO.class))).thenThrow(new RecursoNaoEncontradoException("Terreno não encontrado."));

        mockMvc.perform(put("/api/terrenos/" + terrenoId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(terrenoRequestDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve deletar um terreno e retornar status 204 No Content")
    void deletarTerreno_Success() throws Exception {
        doNothing().when(terrenoService).deletar(terrenoId);

        mockMvc.perform(delete("/api/terrenos/" + terrenoId))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Deve retornar status 404 Not Found ao tentar deletar terreno inexistente")
    void deletarTerreno_NotFound() throws Exception {
        doThrow(new RecursoNaoEncontradoException("Terreno não encontrado.")).when(terrenoService).deletar(terrenoId);

        mockMvc.perform(delete("/api/terrenos/" + terrenoId))
                .andExpect(status().isNotFound());
    }
}