package com.greenew.arvores.controller;

import com.greenew.arvores.model.dto.BiomaDTO;
import com.greenew.arvores.service.BiomaService;
import jakarta.validation.Valid;
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
    public ResponseEntity<BiomaDTO> criarBioma(@Valid @RequestBody BiomaDTO biomaDTO) {
        BiomaDTO biomaCriado = biomaService.criar(biomaDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(biomaCriado);
    }

    @GetMapping
    public ResponseEntity<List<BiomaDTO>> buscarTodosBiomas() {
        List<BiomaDTO> biomas = biomaService.buscarTodos();
        return ResponseEntity.ok(biomas);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BiomaDTO> atualizarBioma(@PathVariable Long id, @Valid @RequestBody BiomaDTO biomaDTO) {
        BiomaDTO biomaAtualizado = biomaService.atualizar(id, biomaDTO);
        return ResponseEntity.ok(biomaAtualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarBioma(@PathVariable Long id) {
        biomaService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}