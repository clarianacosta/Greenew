package com.greenew.relatorios.controller;

import com.greenew.relatorios.model.dto.RelatorioGHGRequestDTO;
import com.greenew.relatorios.model.dto.RelatorioGHGResponseDTO;
import com.greenew.relatorios.service.RelatorioGHGService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/api/relatorios")
public class RelatorioGHGController {

    private final RelatorioGHGService service;

    public RelatorioGHGController(RelatorioGHGService service) {
        this.service = service;
    }

    /**
     * Cria um novo relatório (ainda sem cálculo) associado a uma empresa.
     */
    @PostMapping
    public ResponseEntity<RelatorioGHGResponseDTO> criarRelatorio(@Valid @RequestBody RelatorioGHGRequestDTO requestDTO) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.criarRelatorio(requestDTO));
    }

    /**
     * Busca um relatório específico pelo seu ID.
     */
    @GetMapping("/{id}")
    public ResponseEntity<RelatorioGHGResponseDTO> buscarPorId(@PathVariable UUID id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    /**
     * Busca todos os relatórios de uma empresa específica.
     * Exemplo de chamada: GET /api/relatorios?empresaId=...
     */
    @GetMapping
    public ResponseEntity<List<RelatorioGHGResponseDTO>> buscarRelatoriosPorEmpresa(@RequestParam UUID empresaId) {
        List<RelatorioGHGResponseDTO> relatorios = service.buscarTodosPorEmpresaId(empresaId);
        return ResponseEntity.ok(relatorios);
    }

    /**
     * Ação de finalizar um relatório: dispara o cálculo de emissões e a geração
     * da recomendação de compensação.
     *
     * @param relatorioId O ID do relatório a ser finalizado.
     * @param terrenoId O ID do terreno para basear a recomendação.
     * @param escopos Um JSON array (ex: [1, 2] ou [1, 2, 3]) com os escopos a incluir.
     */
    @PostMapping("/{relatorioId}/finalizar")
    public ResponseEntity<RelatorioGHGResponseDTO> finalizarRelatorio(
            @PathVariable UUID relatorioId,
            @RequestParam UUID terrenoId,
            @RequestBody Set<Integer> escopos) {

        RelatorioGHGResponseDTO relatorioFinalizado = service.finalizarRelatorio(relatorioId, terrenoId, escopos);
        return ResponseEntity.ok(relatorioFinalizado);
    }
}