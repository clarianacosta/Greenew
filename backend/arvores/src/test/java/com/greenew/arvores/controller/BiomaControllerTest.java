package com.greenew.arvores.controller;
import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.greenew.arvores.exception.RecursoNaoEncontradoException;
import com.greenew.arvores.model.dto.BiomaRequestDTO;
import com.greenew.arvores.model.dto.BiomaResponseDTO;
import com.greenew.arvores.service.BiomaService;
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

@WebMvcTest(BiomaController.class)
@DisplayName("Testes para a classe BiomaController")
class BiomaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BiomaService biomaService;

    private BiomaRequestDTO biomaRequestDTO; // NOVO: Objeto usado na requisição (POST/PUT body)
    private BiomaResponseDTO biomaResponseDTO;
    private UUID biomaId;

    @BeforeEach
    void setUp() {
        biomaId = UUID.randomUUID();

        // Dados de ENTRADA (sem ID)
        biomaRequestDTO = new BiomaRequestDTO("Cerrado", "Bioma de savana.");

        // Dados de SAÍDA (com ID)
        biomaResponseDTO = new BiomaResponseDTO(biomaId, "Cerrado", "Bioma de savana.");
    }

    @Test
    @DisplayName("Deve criar um novo bioma e retornar status 201 Created")
    void criarBioma_Success() throws Exception {
        // O serviço espera BiomaRequestDTO (entrada) e retorna BiomaResponseDTO (saída)
        when(biomaService.criar(any(BiomaRequestDTO.class))).thenReturn(biomaResponseDTO);

        mockMvc.perform(post("/api/biomas")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(biomaRequestDTO))) // Usa RequestDTO
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(biomaId.toString()))
                .andExpect(jsonPath("$.nome").value("Cerrado"));
    }

    @Test
    @DisplayName("Deve buscar todos os biomas e retornar status 200 OK")
    void buscarTodosBiomas_Success() throws Exception {
        when(biomaService.buscarTodos()).thenReturn(List.of(biomaResponseDTO));

        mockMvc.perform(get("/api/biomas"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(biomaId.toString()));
    }

    @Test
    @DisplayName("Deve buscar bioma por ID e retornar status 200 OK")
    void buscarBiomaPorId_Success() throws Exception {
        when(biomaService.buscarPorId(biomaId)).thenReturn(biomaResponseDTO);

        mockMvc.perform(get("/api/biomas/" + biomaId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(biomaId.toString()))
                .andExpect(jsonPath("$.nome").value("Cerrado"));
    }


    @Test
    @DisplayName("Deve atualizar um bioma e retornar status 200 OK")
    void atualizarBioma_Success() throws Exception {
        // O serviço espera BiomaRequestDTO (entrada) e retorna BiomaResponseDTO (saída)
        when(biomaService.atualizar(any(UUID.class), any(BiomaRequestDTO.class))).thenReturn(biomaResponseDTO);

        mockMvc.perform(put("/api/biomas/" + biomaId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(biomaRequestDTO))) // Usa RequestDTO
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(biomaId.toString()));
    }

    @Test
    @DisplayName("Deve retornar status 404 Not Found ao tentar atualizar um bioma inexistente")
    void atualizarBioma_NotFound() throws Exception {
        when(biomaService.atualizar(any(UUID.class), any(BiomaRequestDTO.class))).thenThrow(new RecursoNaoEncontradoException("Bioma não encontrado."));

        mockMvc.perform(put("/api/biomas/" + biomaId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(biomaRequestDTO))) // Usa RequestDTO
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve deletar um bioma e retornar status 204 No Content")
    void deletarBioma_Success() throws Exception {
        doNothing().when(biomaService).deletar(any(UUID.class));

        mockMvc.perform(delete("/api/biomas/" + biomaId))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Deve retornar status 404 Not Found ao tentar deletar um bioma inexistente")
    void deletarBioma_NotFound() throws Exception {
        doThrow(new RecursoNaoEncontradoException("Bioma não encontrado.")).when(biomaService).deletar(any(UUID.class));

        mockMvc.perform(delete("/api/biomas/" + biomaId))
                .andExpect(status().isNotFound());
    }
}