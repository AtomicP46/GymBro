package com.gymbro.controller;

import com.gymbro.dto.EquipamentoDTO;
import com.gymbro.model.Equipamento;
import com.gymbro.service.EquipamentoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("/api/equipamentos")
public class EquipamentoController {

    private final EquipamentoService service;

    public EquipamentoController(EquipamentoService service) {
        this.service = service;
    }

    // Cria um novo equipamento
    @PostMapping
    public ResponseEntity<EquipamentoDTO> criar(@Valid @RequestBody EquipamentoDTO dto) {
        EquipamentoDTO criado = service.criarEquipamento(dto);
        URI uri = URI.create("/api/equipamentos/" + criado.getId());
        return ResponseEntity.created(uri).body(criado);
    }

    // Lista todos os equipamentos
    @GetMapping
    public ResponseEntity<List<EquipamentoDTO>> listar() {
        List<EquipamentoDTO> lista = service.listarTodos();
        return ResponseEntity.ok(lista);
    }

    // Busca por ID
    @GetMapping("/{id}")
    public ResponseEntity<EquipamentoDTO> buscarPorId(@PathVariable Long id) {
        try {
            EquipamentoDTO dto = service.buscarPorId(id);
            return ResponseEntity.ok(dto);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(NOT_FOUND, e.getMessage(), e);
        }
    }

    // Busca por nome (parcial, case-insensitive)
    @GetMapping("/busca")
    public ResponseEntity<List<EquipamentoDTO>> buscarPorNome(@RequestParam String nome) {
        List<EquipamentoDTO> lista = service.buscarPorNome(nome);
        return ResponseEntity.ok(lista);
    }

    // Atualiza equipamento existente
    @PutMapping("/{id}")
    public ResponseEntity<EquipamentoDTO> atualizar(
        @PathVariable Long id,
        @Valid @RequestBody EquipamentoDTO dto
    ) {
        try {
            EquipamentoDTO atualizado = service.atualizarEquipamento(id, dto);
            return ResponseEntity.ok(atualizado);
        } catch (IllegalArgumentException e) {
            // se não encontrado ou validação
            throw new ResponseStatusException(BAD_REQUEST, e.getMessage(), e);
        }
    }

    // Exclui equipamento
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        try {
            service.deletarEquipamento(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(NOT_FOUND, e.getMessage(), e);
        }
    }

    // Busca por faixa de peso
    @GetMapping("/faixa-peso")
    public ResponseEntity<List<EquipamentoDTO>> buscarPorFaixaPeso(
        @RequestParam("min") Float pesoMin,
        @RequestParam("max") Float pesoMax
    ) {
        List<EquipamentoDTO> lista = service.buscarPorFaixaPeso(pesoMin, pesoMax);
        return ResponseEntity.ok(lista);
    }

    // Estatísticas simples sem criar DTO extra
    @GetMapping("/estatisticas")
    public ResponseEntity<Map<String, Object>> estatisticas() {
        long total  = service.contarEquipamentos();
        List<EquipamentoDTO> todos = service.listarTodos();

        long comPeso = todos.stream()
            .filter(e -> e.getPesoEquip() != null)
            .count();
        long semPeso = total - comPeso;
        double pesoMedio = todos.stream()
            .filter(e -> e.getPesoEquip() != null)
            .mapToDouble(EquipamentoDTO::getPesoEquip)
            .average()
            .orElse(0.0);

        Map<String, Object> stats = new HashMap<>();
        stats.put("total",     total);
        stats.put("comPeso",   comPeso);
        stats.put("semPeso",   semPeso);
        stats.put("pesoMedio", pesoMedio);

        return ResponseEntity.ok(stats);
    }
}