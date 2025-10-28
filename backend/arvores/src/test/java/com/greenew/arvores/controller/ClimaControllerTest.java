package com.greenew.arvores.controller;
import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.greenew.arvores.exception.RecursoNaoEncontradoException;
import com.greenew.arvores.model.dto.ClimaRequestDTO; // NOVO: Para entrada
import com.greenew.arvores.model.dto.ClimaResponseDTO; // Para saída
import com.greenew.arvores.service.ClimaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ClimaController.class)
@DisplayName("Testes para a classe ClimaController")
class ClimaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ClimaService climaService;

    private ClimaRequestDTO climaRequestDTO; // Objeto usado no corpo da requisição
    private ClimaResponseDTO climaResponseDTO;
    private UUID climaId;

    @BeforeEach
    void setUp() {
        climaId = UUID.randomUUID();

        // Dados de ENTRADA (sem ID, apenas nome/descrição)
        climaRequestDTO = new ClimaRequestDTO("Tropical", "Clima quente e úmido.");

        // Dados de SAÍDA (com ID)
        climaResponseDTO = new ClimaResponseDTO(climaId, "Tropical", "Clima quente e úmido.");
    }

    @Test
    @DisplayName("Deve criar um novo clima e retornar status 201 Created")
    void criarClima_Success() throws Exception {
        // O serviço espera ClimaRequestDTO na entrada
        when(climaService.criar(any(ClimaRequestDTO.class))).thenReturn(climaResponseDTO);

        mockMvc.perform(post("/api/climas")
                        .contentType(MediaType.APPLICATION_JSON)
                        // Usa ClimaRequestDTO no corpo da requisição
                        .content(objectMapper.writeValueAsString(climaRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(climaId.toString()))
                .andExpect(jsonPath("$.nome").value("Tropical"));
    }

    @Test
    @DisplayName("Deve buscar todos os climas e retornar status 200 OK")
    void buscarTodosClimas_Success() throws Exception {
        when(climaService.buscarTodos()).thenReturn(List.of(climaResponseDTO));

        mockMvc.perform(get("/api/climas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(climaId.toString()));
    }

    @Test
    @DisplayName("Deve buscar clima por ID e retornar status 200 OK")
    void buscarClimaPorId_Success() throws Exception {
        when(climaService.buscarPorId(climaId)).thenReturn(climaResponseDTO);

        mockMvc.perform(get("/api/climas/" + climaId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(climaId.toString()))
                .andExpect(jsonPath("$.nome").value("Tropical"));
    }


    @Test
    @DisplayName("Deve atualizar um clima e retornar status 200 OK")
    void atualizarClima_Success() throws Exception {
        // O serviço espera ClimaRequestDTO na entrada
        when(climaService.atualizar(any(UUID.class), any(ClimaRequestDTO.class))).thenReturn(climaResponseDTO);

        mockMvc.perform(put("/api/climas/" + climaId)
                        .contentType(MediaType.APPLICATION_JSON)
                        // Usa ClimaRequestDTO no corpo da requisição
                        .content(objectMapper.writeValueAsString(climaRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(climaId.toString()));
    }

    @Test
    @DisplayName("Deve retornar status 404 Not Found ao tentar atualizar um clima inexistente")
    void atualizarClima_NotFound() throws Exception {
        when(climaService.atualizar(any(UUID.class), any(ClimaRequestDTO.class))).thenThrow(new RecursoNaoEncontradoException("Clima não encontrado."));

        mockMvc.perform(put("/api/climas/" + climaId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(climaRequestDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve deletar um clima e retornar status 204 No Content")
    void deletarClima_Success() throws Exception {
        doNothing().when(climaService).deletar(any(UUID.class));

        mockMvc.perform(delete("/api/climas/" + climaId))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Deve retornar status 404 Not Found ao tentar deletar um clima inexistente")
    void deletarClima_NotFound() throws Exception {
        doThrow(new RecursoNaoEncontradoException("Clima não encontrado.")).when(climaService).deletar(any(UUID.class));

        mockMvc.perform(delete("/api/climas/" + climaId))
                .andExpect(status().isNotFound());
    }
}