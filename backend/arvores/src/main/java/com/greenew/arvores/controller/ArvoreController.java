package com.greenew.arvores.controller;

import com.greenew.arvores.model.dto.ArvoreRequestDTO;
import com.greenew.arvores.model.dto.ArvoreResponseDTO;
import com.greenew.arvores.service.ArvoreService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/arvores")
public class ArvoreController {

    private final ArvoreService arvoreService;

    public ArvoreController(ArvoreService arvoreService) {
        this.arvoreService = arvoreService;
    }

    @PostMapping
    public ResponseEntity<ArvoreResponseDTO> criarArvore(@Valid @RequestBody ArvoreRequestDTO requestDTO) {
        ArvoreResponseDTO arvoreCriada = arvoreService.criar(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(arvoreCriada);
    }

    @GetMapping
    public ResponseEntity<List<ArvoreResponseDTO>> buscarTodasArvores() {
        List<ArvoreResponseDTO> arvores = arvoreService.buscarTodas();
        return ResponseEntity.ok(arvores);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ArvoreResponseDTO> buscarArvorePorId(@PathVariable Long id) {
        ArvoreResponseDTO arvore = arvoreService.buscarPorId(id);
        return ResponseEntity.ok(arvore);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ArvoreResponseDTO> atualizarArvore(@PathVariable Long id, @Valid @RequestBody ArvoreRequestDTO requestDTO) {
        ArvoreResponseDTO arvoreAtualizada = arvoreService.atualizar(id, requestDTO);
        return ResponseEntity.ok(arvoreAtualizada);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarArvore(@PathVariable Long id) {
        arvoreService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}