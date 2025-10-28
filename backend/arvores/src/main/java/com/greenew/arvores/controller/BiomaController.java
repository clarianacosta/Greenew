package com.greenew.arvores.controller;

import com.greenew.arvores.model.dto.BiomaRequestDTO; // NOVO: Para o corpo da requisição
import com.greenew.arvores.model.dto.BiomaResponseDTO;
import com.greenew.arvores.service.BiomaService;
import jakarta.validation.Valid;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/biomas")
public class BiomaController {

    private final BiomaService biomaService;

    public BiomaController(BiomaService biomaService) {
        this.biomaService = biomaService;
    }

    @PostMapping
    public ResponseEntity<BiomaResponseDTO> criarBioma(@Valid @RequestBody BiomaRequestDTO biomaRequestDTO) {
        BiomaResponseDTO biomaCriado = biomaService.criar(biomaRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(biomaCriado);
    }

    @GetMapping
    public ResponseEntity<List<BiomaResponseDTO>> buscarTodosBiomas() {
        List<BiomaResponseDTO> biomas = biomaService.buscarTodos();
        return ResponseEntity.ok(biomas);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BiomaResponseDTO> buscarBiomaPorId(@PathVariable UUID id) {
        BiomaResponseDTO bioma = biomaService.buscarPorId(id);
        return ResponseEntity.ok(bioma);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BiomaResponseDTO> atualizarBioma(@PathVariable UUID id, @Valid @RequestBody BiomaRequestDTO biomaRequestDTO) {
        BiomaResponseDTO biomaAtualizado = biomaService.atualizar(id, biomaRequestDTO);
        return ResponseEntity.ok(biomaAtualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarBioma(@PathVariable UUID id) {
        biomaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}