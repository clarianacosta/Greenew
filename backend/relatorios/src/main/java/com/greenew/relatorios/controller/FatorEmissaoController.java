package com.greenew.relatorios.controller;

import com.greenew.relatorios.model.dto.FatorEmissaoRequestDTO;
import com.greenew.relatorios.model.dto.FatorEmissaoResponseDTO;
import com.greenew.relatorios.service.FatorEmissaoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/fatores-emissao")
public class FatorEmissaoController {

    private final FatorEmissaoService service;

    public FatorEmissaoController(FatorEmissaoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<FatorEmissaoResponseDTO> criarFator(@Valid @RequestBody FatorEmissaoRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criarFator(dto));
    }

    @GetMapping
    public ResponseEntity<List<FatorEmissaoResponseDTO>> buscarTodos() {
        return ResponseEntity.ok(service.buscarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FatorEmissaoResponseDTO> buscarPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarFator(@PathVariable UUID id) {
        service.deletarFator(id);
        return ResponseEntity.noContent().build();
    }
}