package com.greenew.empresas.controller;

import com.greenew.empresas.model.dto.EmpresaRequestDTO;
import com.greenew.empresas.model.dto.EmpresaResponseDTO;
import com.greenew.empresas.service.EmpresaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Controller REST para gerenciar o recurso Empresa e seus sub-recursos.
 */
@RestController
@RequestMapping("/api/empresas")
public class EmpresaController {

    private final EmpresaService empresaService;

    public EmpresaController(EmpresaService empresaService) {
        this.empresaService = empresaService;
    }

    /**
     * POST /api/empresas : Cria uma nova empresa.
     */
    @PostMapping
    public ResponseEntity<EmpresaResponseDTO> criarEmpresa(@Valid @RequestBody EmpresaRequestDTO requestDTO) {
        EmpresaResponseDTO novaEmpresa = empresaService.criarEmpresa(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(novaEmpresa);
    }

    /**
     * GET /api/empresas : Busca todas as empresas.
     */
    @GetMapping
    public ResponseEntity<List<EmpresaResponseDTO>> buscarTodas() {
        return ResponseEntity.ok(empresaService.buscarTodas());
    }

    /**
     * GET /api/empresas/{id} : Busca uma empresa específica pelo seu ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<EmpresaResponseDTO> buscarPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(empresaService.buscarPorId(id));
    }

    /**
     * PUT /api/empresas/{id} : Atualiza uma empresa existente.
     */
    @PutMapping("/{id}")
    public ResponseEntity<EmpresaResponseDTO> atualizarEmpresa(@PathVariable UUID id, @Valid @RequestBody EmpresaRequestDTO requestDTO) {
        EmpresaResponseDTO empresaAtualizada = empresaService.atualizarEmpresa(id, requestDTO);
        return ResponseEntity.ok(empresaAtualizada);
    }

    /**
     * DELETE /api/empresas/{id} : Deleta uma empresa.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarEmpresa(@PathVariable UUID id) {
        empresaService.deletarEmpresa(id);
        return ResponseEntity.noContent().build(); // Retorna status 204 No Content
    }
}