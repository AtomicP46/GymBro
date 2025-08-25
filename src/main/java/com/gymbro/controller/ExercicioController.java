package com.gymbro.controller;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.gymbro.dto.ExercicioDTO;
import com.gymbro.model.Exercicio;
import com.gymbro.service.ExercicioService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/exercicios")
@CrossOrigin(origins = "*")
public class ExercicioController {

    private final ExercicioService exercicioService;

    @Autowired
    public ExercicioController(ExercicioService exercicioService) {
        this.exercicioService = exercicioService;
    }

    @PostMapping
    public ResponseEntity<Exercicio> criar(@Valid @RequestBody ExercicioDTO dto) {
        try {
            Exercicio criado = exercicioService.criarExercicio(dto);
            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(criado.getId())
                    .toUri();
            return ResponseEntity.created(location).body(criado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Exercicio>> listar() {
        return ResponseEntity.ok(exercicioService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Exercicio> buscarPorId(@PathVariable Long id) {
        try {
            Exercicio exercicio = exercicioService.buscarPorId(id);
            return ResponseEntity.ok(exercicio);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/busca")
    public ResponseEntity<List<Exercicio>> buscarPorNome(@RequestParam String nome) {
        try {
            List<Exercicio> exercicios = exercicioService.buscarPorNome(nome);
            return ResponseEntity.ok(exercicios);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/regiao/{regiao}")
    public ResponseEntity<List<Exercicio>> buscarPorRegiao(@PathVariable String regiao) {
        try {
            List<Exercicio> exercicios = exercicioService.buscarPorRegiao(regiao);
            return ResponseEntity.ok(exercicios);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<Exercicio>> buscarPorTipo(@PathVariable String tipo) {
        try {
            List<Exercicio> exercicios = exercicioService.buscarPorTipo(tipo);
            return ResponseEntity.ok(exercicios);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/equipamento/{equipamentoId}")
    public ResponseEntity<List<Exercicio>> buscarPorEquipamento(@PathVariable Long equipamentoId) {
        try {
            List<Exercicio> exercicios = exercicioService.buscarPorEquipamento(equipamentoId);
            return ResponseEntity.ok(exercicios);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/unilateral/{unilateral}")
    public ResponseEntity<List<Exercicio>> buscarPorUnilateral(@PathVariable Boolean unilateral) {
        try {
            List<Exercicio> exercicios = exercicioService.buscarPorUnilateral(unilateral);
            return ResponseEntity.ok(exercicios);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Exercicio> atualizar(@PathVariable Long id, @Valid @RequestBody ExercicioDTO dto) {
        try {
            Exercicio atualizado = exercicioService.atualizarExercicio(id, dto);
            return ResponseEntity.ok(atualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        try {
            exercicioService.deletarExercicio(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/estatisticas")
    public ResponseEntity<Map<String, Object>> estatisticas() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("total", exercicioService.contarExercicio());
        
        Map<String, Long> porRegiao = new HashMap<>();
        for (String regiao : exercicioService.getRegioesValidas()) {
            porRegiao.put(regiao, exercicioService.contarPorRegiao(regiao));
        }
        stats.put("porRegiao", porRegiao);
        
        long unilaterais = exercicioService.buscarPorUnilateral(true).size();
        long bilaterais = exercicioService.buscarPorUnilateral(false).size();
        stats.put("unilaterais", unilaterais);
        stats.put("bilaterais", bilaterais);
        
        return ResponseEntity.ok(stats);
    }

    @GetMapping("/regioes")
    public ResponseEntity<String[]> getRegioesValidas() {
        return ResponseEntity.ok(exercicioService.getRegioesValidas());
    }

    @GetMapping("/tipos")
    public ResponseEntity<String[]> getTiposValidos() {
        return ResponseEntity.ok(exercicioService.getTiposValidos());
    }
}
