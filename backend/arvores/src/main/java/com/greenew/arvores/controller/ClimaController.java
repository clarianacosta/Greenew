package com.greenew.arvores.controller;

import com.greenew.arvores.model.dto.ClimaDTO;
import com.greenew.arvores.service.ClimaService;
import jakarta.validation.Valid;
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
    public ResponseEntity<ClimaDTO> criarClima(@Valid @RequestBody ClimaDTO climaDTO) {
        ClimaDTO climaCriado = climaService.criar(climaDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(climaCriado);
    }

    @GetMapping
    public ResponseEntity<List<ClimaDTO>> buscarTodosClimas() {
        List<ClimaDTO> climas = climaService.buscarTodos();
        return ResponseEntity.ok(climas);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClimaDTO> atualizarClima(@PathVariable Long id, @Valid @RequestBody ClimaDTO climaDTO) {
        ClimaDTO climaAtualizado = climaService.atualizar(id, climaDTO);
        return ResponseEntity.ok(climaAtualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarClima(@PathVariable Long id) {
        climaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}