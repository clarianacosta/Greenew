package com.greenew.produtores.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.greenew.produtores.exception.RecursoNaoEncontradoException;
import com.greenew.produtores.model.dto.ProdutorRequestDTO;
import com.greenew.produtores.model.dto.ProdutorResponseDTO;
import com.greenew.produtores.service.ProdutorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Collections;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProdutorController.class)
@DisplayName("Testes para a classe ProdutorController")
class ProdutorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProdutorService produtorService;

    private ProdutorRequestDTO produtorRequestDTO;
    private ProdutorResponseDTO produtorResponseDTO;
    private UUID produtorId;

    @BeforeEach
    void setUp() {
        produtorId = UUID.randomUUID();

        produtorRequestDTO = new ProdutorRequestDTO();
        produtorRequestDTO.setNomeCompleto("João da Silva");
        produtorRequestDTO.setEmail("joao.silva@teste.com");
        produtorRequestDTO.setCelular("11987654321");

        produtorResponseDTO = new ProdutorResponseDTO(
                produtorId, "João da Silva", "joao.silva@teste.com", "11987654321", Collections.emptySet()
        );
    }

    @Test
    @DisplayName("Deve criar um novo produtor e retornar status 201 Created")
    void criarProdutor_Success() throws Exception {
        when(produtorService.criar(any(ProdutorRequestDTO.class))).thenReturn(produtorResponseDTO);

        mockMvc.perform(post("/api/produtores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(produtorRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(produtorId.toString()))
                .andExpect(jsonPath("$.nomeCompleto").value("João da Silva"));
    }

    @Test
    @DisplayName("Deve buscar todos os produtores e retornar status 200 OK")
    void buscarTodosProdutores_Success() throws Exception {
        when(produtorService.buscarTodos()).thenReturn(List.of(produtorResponseDTO));

        mockMvc.perform(get("/api/produtores"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(produtorId.toString()));
    }

    @Test
    @DisplayName("Deve buscar produtor por ID e retornar status 200 OK")
    void buscarProdutorPorId_Success() throws Exception {
        when(produtorService.buscarPorId(produtorId)).thenReturn(produtorResponseDTO);

        mockMvc.perform(get("/api/produtores/" + produtorId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(produtorId.toString()));
    }

    @Test
    @DisplayName("Deve retornar status 404 Not Found ao buscar produtor inexistente")
    void buscarProdutorPorId_NotFound() throws Exception {
        when(produtorService.buscarPorId(produtorId)).thenThrow(new RecursoNaoEncontradoException("Produtor não encontrado."));

        mockMvc.perform(get("/api/produtores/" + produtorId))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve atualizar um produtor e retornar status 200 OK")
    void atualizarProdutor_Success() throws Exception {
        when(produtorService.atualizar(eq(produtorId), any(ProdutorRequestDTO.class))).thenReturn(produtorResponseDTO);

        mockMvc.perform(put("/api/produtores/" + produtorId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(produtorRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(produtorId.toString()));
    }

    @Test
    @DisplayName("Deve retornar status 404 Not Found ao tentar atualizar produtor inexistente")
    void atualizarProdutor_NotFound() throws Exception {
        when(produtorService.atualizar(eq(produtorId), any(ProdutorRequestDTO.class))).thenThrow(new RecursoNaoEncontradoException("Produtor não encontrado."));

        mockMvc.perform(put("/api/produtores/" + produtorId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(produtorRequestDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve deletar um produtor e retornar status 204 No Content")
    void deletarProdutor_Success() throws Exception {
        doNothing().when(produtorService).deletar(produtorId);

        mockMvc.perform(delete("/api/produtores/" + produtorId))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Deve retornar status 404 Not Found ao tentar deletar produtor inexistente")
    void deletarProdutor_NotFound() throws Exception {
        doThrow(new RecursoNaoEncontradoException("Produtor não encontrado.")).when(produtorService).deletar(produtorId);

        mockMvc.perform(delete("/api/produtores/" + produtorId))
                .andExpect(status().isNotFound());
    }
}