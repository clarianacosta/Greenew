package com.greenew.arvores.controller;

import com.greenew.arvores.model.dto.ClimaRequestDTO; // NOVO: Para o corpo da requisição
import com.greenew.arvores.model.dto.ClimaResponseDTO;
import com.greenew.arvores.service.ClimaService;
import jakarta.validation.Valid;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/climas")
public class ClimaController {

    private final ClimaService climaService;

    public ClimaController(ClimaService climaService) {
        this.climaService = climaService;
    }

    @PostMapping
    public ResponseEntity<ClimaResponseDTO> criarClima(@Valid @RequestBody ClimaRequestDTO climaRequestDTO) {
        ClimaResponseDTO climaCriado = climaService.criar(climaRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(climaCriado);
    }

    @GetMapping
    public ResponseEntity<List<ClimaResponseDTO>> buscarTodosClimas() {
        List<ClimaResponseDTO> climas = climaService.buscarTodos();
        return ResponseEntity.ok(climas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClimaResponseDTO> buscarClimaPorId(@PathVariable UUID id) {
        ClimaResponseDTO clima = climaService.buscarPorId(id);
        return ResponseEntity.ok(clima);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClimaResponseDTO> atualizarClima(@PathVariable UUID id, @Valid @RequestBody ClimaRequestDTO climaRequestDTO) {
        ClimaResponseDTO climaAtualizado = climaService.atualizar(id, climaRequestDTO);
        return ResponseEntity.ok(climaAtualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarClima(@PathVariable UUID id) {
        climaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}