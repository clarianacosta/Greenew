package com.greenew.produtores.controller;

import com.greenew.produtores.model.dto.ProdutorRequestDTO;
import com.greenew.produtores.model.dto.ProdutorResponseDTO;
import com.greenew.produtores.service.ProdutorService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/produtores")
public class ProdutorController {

    private final ProdutorService produtorService;

    public ProdutorController(ProdutorService produtorService) {
        this.produtorService = produtorService;
    }

    @PostMapping
    public ResponseEntity<ProdutorResponseDTO> criarProdutor(@Valid @RequestBody ProdutorRequestDTO requestDTO) {
        ProdutorResponseDTO produtorCriado = produtorService.criar(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(produtorCriado);
    }

    @GetMapping
    public ResponseEntity<List<ProdutorResponseDTO>> buscarTodosProdutores() {
        List<ProdutorResponseDTO> produtores = produtorService.buscarTodos();
        return ResponseEntity.ok(produtores);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProdutorResponseDTO> buscarProdutorPorId(@PathVariable UUID id) {
        ProdutorResponseDTO produtor = produtorService.buscarPorId(id);
        return ResponseEntity.ok(produtor);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProdutorResponseDTO> atualizarProdutor(@PathVariable UUID id, @Valid @RequestBody ProdutorRequestDTO requestDTO) {
        ProdutorResponseDTO produtorAtualizado = produtorService.atualizar(id, requestDTO);
        return ResponseEntity.ok(produtorAtualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarProdutor(@PathVariable UUID id) {
        produtorService.deletar(id);
        return ResponseEntity.noContent().build();
    }
}