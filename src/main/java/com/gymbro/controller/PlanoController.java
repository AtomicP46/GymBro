package com.gymbro.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gymbro.dto.PlanoDTO;
import com.gymbro.dto.PlanoExercicioDTO;
import com.gymbro.enums.TipoCriador;
import com.gymbro.model.Plano;
import com.gymbro.model.PlanoExercicio;
import com.gymbro.service.PlanoExercicioService;
import com.gymbro.service.PlanoService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

@RestController
@RequestMapping("/api/planos")
@Validated
@CrossOrigin(origins = "*")
public class PlanoController {
    
    @Autowired
    private PlanoService planoService;
    
    @Autowired
    private PlanoExercicioService planoExercicioService;

    @PostMapping
    public ResponseEntity<Plano> criarPlano(@Valid @RequestBody PlanoDTO planoDTO) {
        Plano plano = planoService.criarPlano(planoDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(plano);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Plano> buscarPorId(@PathVariable @NotNull Long id) {
        Plano plano = planoService.buscarPorId(id);
        return ResponseEntity.ok(plano);
    }

    @GetMapping
    public ResponseEntity<List<Plano>> listarTodos() {
        List<Plano> planos = planoService.listarTodos();
        return ResponseEntity.ok(planos);
    }

    @GetMapping("/criador/{criadorId}")
    public ResponseEntity<List<Plano>> listarPorCriador(
            @PathVariable @NotNull Long criadorId,
            @RequestParam @NotNull TipoCriador tipoCriador) {
        List<Plano> planos = planoService.listarPorCriador(criadorId, tipoCriador);
        return ResponseEntity.ok(planos);
    }

    @GetMapping("/publicos")
    public ResponseEntity<List<Plano>> listarPublicos() {
        List<Plano> planos = planoService.listarPublicos();
        return ResponseEntity.ok(planos);
    }

    @GetMapping("/buscar")
    public ResponseEntity<List<Plano>> buscarPorNome(@RequestParam @NotNull String nome) {
        List<Plano> planos = planoService.buscarPorNome(nome);
        return ResponseEntity.ok(planos);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Plano> atualizarPlano(
            @PathVariable @NotNull Long id,
            @Valid @RequestBody PlanoDTO planoDTO,
            @RequestParam @NotNull Long criadorId,
            @RequestParam @NotNull TipoCriador tipoCriador) {
        Plano plano = planoService.atualizarPlano(id, planoDTO, criadorId, tipoCriador);
        return ResponseEntity.ok(plano);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluirPlano(
            @PathVariable @NotNull Long id,
            @RequestParam @NotNull Long criadorId,
            @RequestParam @NotNull TipoCriador tipoCriador) {
        planoService.excluirPlano(id, criadorId, tipoCriador);
        return ResponseEntity.noContent().build();
    }

    // Endpoints para gerenciar exercícios do plano
    @PostMapping("/{planoId}/exercicios")
    public ResponseEntity<PlanoExercicio> adicionarExercicio(
            @PathVariable @NotNull Long planoId,
            @Valid @RequestBody PlanoExercicioDTO dto) {
        dto.setPlanoId(planoId);
        PlanoExercicio planoExercicio = planoExercicioService.adicionarExercicio(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(planoExercicio);
    }

    @GetMapping("/{planoId}/exercicios")
    public ResponseEntity<List<PlanoExercicio>> listarExerciciosDoPlano(@PathVariable @NotNull Long planoId) {
        List<PlanoExercicio> exercicios = planoExercicioService.listarPorPlano(planoId);
        return ResponseEntity.ok(exercicios);
    }

    @GetMapping("/{planoId}/exercicios/aquecimento")
    public ResponseEntity<List<PlanoExercicio>> listarAquecimento(@PathVariable @NotNull Long planoId) {
        List<PlanoExercicio> exercicios = planoExercicioService.listarAquecimentoPorPlano(planoId);
        return ResponseEntity.ok(exercicios);
    }

    @GetMapping("/{planoId}/exercicios/principais")
    public ResponseEntity<List<PlanoExercicio>> listarPrincipais(@PathVariable @NotNull Long planoId) {
        List<PlanoExercicio> exercicios = planoExercicioService.listarPrincipaisPorPlano(planoId);
        return ResponseEntity.ok(exercicios);
    }

    @PutMapping("/exercicios/{exercicioId}")
    public ResponseEntity<PlanoExercicio> atualizarExercicio(
            @PathVariable @NotNull Long exercicioId,
            @Valid @RequestBody PlanoExercicioDTO dto) {
        PlanoExercicio planoExercicio = planoExercicioService.atualizarExercicio(exercicioId, dto);
        return ResponseEntity.ok(planoExercicio);
    }

    @DeleteMapping("/exercicios/{exercicioId}")
    public ResponseEntity<Void> removerExercicio(@PathVariable @NotNull Long exercicioId) {
        planoExercicioService.removerExercicio(exercicioId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{planoId}/exercicios")
    public ResponseEntity<Void> removerTodosExercicios(@PathVariable @NotNull Long planoId) {
        planoExercicioService.removerTodosExerciciosDoPlano(planoId);
        return ResponseEntity.noContent().build();
    }
}
