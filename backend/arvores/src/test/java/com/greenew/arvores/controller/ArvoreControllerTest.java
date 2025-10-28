package com.greenew.arvores.controller;
import java.util.UUID;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.greenew.arvores.exception.RecursoNaoEncontradoException;
import com.greenew.arvores.model.dto.ArvoreRequestDTO;
import com.greenew.arvores.model.dto.ArvoreResponseDTO;
import com.greenew.arvores.service.ArvoreService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ArvoreController.class)
@DisplayName("Testes para a classe ArvoreController")
class ArvoreControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ArvoreService arvoreService;

    private ArvoreRequestDTO arvoreRequestDTO;
    private ArvoreResponseDTO arvoreResponseDTO;
    private UUID arvoreId; // ID da árvore para usar nos testes

    @BeforeEach
    void setUp() {
        // Gerar um único UUID para a execução de cada teste
        arvoreId = UUID.randomUUID();

        // Inicialização de arvoreRequestDTO usando setters
        arvoreRequestDTO = new ArvoreRequestDTO();
        arvoreRequestDTO.setNomePopular("Ipê Amarelo");
        arvoreRequestDTO.setNomeCientifico("Handroanthus albus");
        arvoreRequestDTO.setTaxaAbsorcaoCo2Anual(new BigDecimal("22.50"));
        arvoreRequestDTO.setCustoMedioMuda(new BigDecimal("45.00"));
        arvoreRequestDTO.setTempoMaturidadeAnos(15);
        arvoreRequestDTO.setAlturaMediaM(new BigDecimal("18.50"));
        arvoreRequestDTO.setDiametroCopaMedioM(new BigDecimal("12.00"));
        arvoreRequestDTO.setBiomasIds(Set.of(UUID.randomUUID(), UUID.randomUUID()));
        arvoreRequestDTO.setClimasIds(Set.of(UUID.randomUUID()));

        // Inicialização de arvoreResponseDTO com o ID gerado no setUp
        arvoreResponseDTO = new ArvoreResponseDTO(
                arvoreId, "Ipê Amarelo", "Handroanthus albus", new BigDecimal("22.50"), null, null, null, null, Collections.emptySet(), Collections.emptySet()
        );
    }

    @Test
    @DisplayName("Deve criar uma nova árvore e retornar status 201 Created")
    void criarArvore_Success() throws Exception {
        when(arvoreService.criar(any(ArvoreRequestDTO.class))).thenReturn(arvoreResponseDTO);

        mockMvc.perform(post("/api/arvores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(arvoreRequestDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(arvoreId.toString()))
                .andExpect(jsonPath("$.nomePopular").value("Ipê Amarelo"));
    }

    @Test
    @DisplayName("Deve buscar todas as árvores e retornar status 200 OK")
    void buscarTodasArvores_Success() throws Exception {
        when(arvoreService.buscarTodas()).thenReturn(List.of(arvoreResponseDTO));

        mockMvc.perform(get("/api/arvores"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(arvoreId.toString()));
    }

    @Test
    @DisplayName("Deve buscar árvore por ID e retornar status 200 OK")
    void buscarArvorePorId_Success() throws Exception {
        when(arvoreService.buscarPorId(any(UUID.class))).thenReturn(arvoreResponseDTO);

        mockMvc.perform(get("/api/arvores/" + arvoreId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(arvoreId.toString()));
    }

    @Test
    @DisplayName("Deve retornar status 404 Not Found se a árvore não for encontrada")
    void buscarArvorePorId_NotFound() throws Exception {
        when(arvoreService.buscarPorId(any(UUID.class))).thenThrow(new RecursoNaoEncontradoException("Árvore não encontrada."));

        mockMvc.perform(get("/api/arvores/" + arvoreId))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve atualizar uma árvore e retornar status 200 OK")
    void atualizarArvore_Success() throws Exception {
        when(arvoreService.atualizar(any(UUID.class), any(ArvoreRequestDTO.class))).thenReturn(arvoreResponseDTO);

        mockMvc.perform(put("/api/arvores/" + arvoreId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(arvoreRequestDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(arvoreId.toString()));
    }

    @Test
    @DisplayName("Deve retornar status 404 Not Found ao tentar atualizar uma árvore inexistente")
    void atualizarArvore_NotFound() throws Exception {
        when(arvoreService.atualizar(any(UUID.class), any(ArvoreRequestDTO.class))).thenThrow(new RecursoNaoEncontradoException("Árvore não encontrada."));

        mockMvc.perform(put("/api/arvores/" + arvoreId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(arvoreRequestDTO)))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Deve deletar uma árvore e retornar status 204 No Content")
    void deletarArvore_Success() throws Exception {
        doNothing().when(arvoreService).deletar(any(UUID.class));

        mockMvc.perform(delete("/api/arvores/" + arvoreId))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Deve retornar status 404 Not Found ao tentar deletar uma árvore inexistente")
    void deletarArvore_NotFound() throws Exception {
        doThrow(new RecursoNaoEncontradoException("Árvore não encontrada.")).when(arvoreService).deletar(any(UUID.class));

        mockMvc.perform(delete("/api/arvores/" + arvoreId))
                .andExpect(status().isNotFound());
    }
}