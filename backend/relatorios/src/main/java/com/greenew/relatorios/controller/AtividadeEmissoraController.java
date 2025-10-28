package com.greenew.relatorios.controller;

import com.greenew.relatorios.model.dto.AtividadeEmissoraRequestDTO;
import com.greenew.relatorios.model.dto.AtividadeEmissoraResponseDTO;
import com.greenew.relatorios.service.AtividadeEmissoraService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/relatorios/{relatorioId}/atividades")
public class AtividadeEmissoraController {

    private final AtividadeEmissoraService service;

    public AtividadeEmissoraController(AtividadeEmissoraService service) {
        this.service = service;
    }

    /**
     * Endpoint para adicionar uma nova atividade de emissão a um relatório existente.
     */
    @PostMapping
    public ResponseEntity<AtividadeEmissoraResponseDTO> adicionarAtividade(
            @PathVariable UUID relatorioId,
            @Valid @RequestBody AtividadeEmissoraRequestDTO requestDTO) {

        AtividadeEmissoraResponseDTO novaAtividade = service.adicionarAtividade(relatorioId, requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaAtividade);
    }
}