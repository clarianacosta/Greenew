package com.greenew.produtores.controller;

import com.greenew.produtores.model.dto.TerrenoRequestDTO;
import com.greenew.produtores.model.dto.TerrenoResponseDTO;
import com.greenew.produtores.service.TerrenoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/terrenos")
public class TerrenoController {

    private final TerrenoService terrenoService;

    public TerrenoController(TerrenoService terrenoService) {
        this.terrenoService = terrenoService;
    }

    @PostMapping
    public ResponseEntity<TerrenoResponseDTO> criarTerreno(@Valid @RequestBody TerrenoRequestDTO requestDTO) {
        TerrenoResponseDTO terrenoCriado = terrenoService.criar(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(terrenoCriado);
    }

    @GetMapping
    public ResponseEntity<List<TerrenoResponseDTO>> buscarTodosTerrenos() {
        List<TerrenoResponseDTO> terrenos = terrenoService.buscarTodos();
        return ResponseEntity.ok(terrenos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TerrenoResponseDTO> buscarTerrenoPorId(@PathVariable UUID id) {
        TerrenoResponseDTO terreno = terrenoService.buscarPorId(id);
        return ResponseEntity.ok(terreno);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TerrenoResponseDTO> atualizarTerreno(@PathVariable UUID id, @Valid @RequestBody TerrenoRequestDTO requestDTO) {
        TerrenoResponseDTO terrenoAtualizado = terrenoService.atualizar(id, requestDTO);
        return ResponseEntity.ok(terrenoAtualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarTerreno(@PathVariable UUID id) {
        terrenoService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}