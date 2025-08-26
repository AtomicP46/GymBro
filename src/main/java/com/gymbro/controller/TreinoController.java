package com.gymbro.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

import com.gymbro.dto.ExercicioProgressoDTO;
import com.gymbro.dto.TreinoDTO;
import com.gymbro.dto.TreinoExercicioDTO;
import com.gymbro.model.Treino;
import com.gymbro.model.TreinoExercicio;
import com.gymbro.service.TreinoExercicioService;
import com.gymbro.service.TreinoService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/treinos")
@CrossOrigin(origins = "*")
public class TreinoController {
    
    @Autowired
    private TreinoService treinoService;
    
    @Autowired
    private TreinoExercicioService treinoExercicioService;

    @PostMapping
    public ResponseEntity<Treino> criarTreino(@Valid @RequestBody TreinoDTO treinoDTO) {
        try {
            Treino treino = treinoService.criarTreino(treinoDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(treino);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<Treino>> listarTodosTreinos() {
        List<Treino> treinos = treinoService.listarTodosTreinos();
        return ResponseEntity.ok(treinos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Treino> buscarTreinoPorId(@PathVariable Long id) {
        Optional<Treino> treino = treinoService.buscarTreinoPorId(id);
        return treino.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Treino>> listarTreinosPorUsuario(@PathVariable Long usuarioId) {
        try {
            List<Treino> treinos = treinoService.listarTreinosPorUsuario(usuarioId);
            return ResponseEntity.ok(treinos);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/personal/{personalId}")
    public ResponseEntity<List<Treino>> listarTreinosPorPersonal(@PathVariable Long personalId) {
        try {
            List<Treino> treinos = treinoService.listarTreinosPorPersonal(personalId);
            return ResponseEntity.ok(treinos);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/em-andamento")
    public ResponseEntity<List<Treino>> listarTreinosEmAndamento() {
        List<Treino> treinos = treinoService.listarTreinosEmAndamento();
        return ResponseEntity.ok(treinos);
    }

    @PutMapping("/{id}/iniciar")
    public ResponseEntity<Treino> iniciarTreino(@PathVariable Long id) {
        try {
            Treino treino = treinoService.iniciarTreino(id);
            return ResponseEntity.ok(treino);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.unprocessableEntity().build();
        }
    }

    @PutMapping("/{id}/finalizar")
    public ResponseEntity<Treino> finalizarTreino(@PathVariable Long id) {
        try {
            Treino treino = treinoService.finalizarTreino(id);
            return ResponseEntity.ok(treino);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.unprocessableEntity().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Treino> atualizarTreino(@PathVariable Long id, @Valid @RequestBody TreinoDTO treinoDTO) {
        try {
            Treino treino = treinoService.atualizarTreino(id, treinoDTO);
            return ResponseEntity.ok(treino);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.unprocessableEntity().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarTreino(@PathVariable Long id) {
        try {
            treinoService.deletarTreino(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.unprocessableEntity().build();
        }
    }

    // Endpoints para TreinoExercicio
    @PostMapping("/{treinoId}/exercicios")
    public ResponseEntity<TreinoExercicio> adicionarExercicioAoTreino(
            @PathVariable Long treinoId, 
            @Valid @RequestBody TreinoExercicioDTO treinoExercicioDTO) {
        try {
            treinoExercicioDTO.setTreinoId(treinoId);
            TreinoExercicio treinoExercicio = treinoExercicioService.adicionarExercicioAoTreino(treinoExercicioDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(treinoExercicio);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{treinoId}/exercicios")
    public ResponseEntity<List<TreinoExercicio>> listarExerciciosDoTreino(@PathVariable Long treinoId) {
        try {
            List<TreinoExercicio> exercicios = treinoExercicioService.listarExerciciosDoTreino(treinoId);
            return ResponseEntity.ok(exercicios);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/exercicios/{exercicioId}")
    public ResponseEntity<TreinoExercicio> atualizarExercicioDoTreino(
            @PathVariable Long exercicioId, 
            @Valid @RequestBody TreinoExercicioDTO treinoExercicioDTO) {
        try {
            TreinoExercicio treinoExercicio = treinoExercicioService.atualizarExercicioDoTreino(exercicioId, treinoExercicioDTO);
            return ResponseEntity.ok(treinoExercicio);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/exercicios/{exercicioId}")
    public ResponseEntity<Void> removerExercicioDoTreino(@PathVariable Long exercicioId) {
        try {
            treinoExercicioService.removerExercicioDoTreino(exercicioId);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/exercicios/{exercicioId}/execucao")
    public ResponseEntity<TreinoExercicio> registrarExecucao(
            @PathVariable Long exercicioId,
            @RequestParam(required = false) Integer repeticoes,
            @RequestParam(required = false) Float pesoUsado,
            @RequestParam(required = false) String anotacoes) {
        try {
            TreinoExercicio treinoExercicio = treinoExercicioService.registrarExecucao(
                exercicioId, repeticoes, pesoUsado, anotacoes);
            return ResponseEntity.ok(treinoExercicio);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/relatorio/progresso")
    public ResponseEntity<List<ExercicioProgressoDTO>> gerarRelatorioProgressoExercicio(
            @RequestParam Long usuarioId, 
            @RequestParam Long exercicioId) {
        List<ExercicioProgressoDTO> relatorio = treinoExercicioService.gerarRelatorioProgressoExercicio(usuarioId, exercicioId);
        return ResponseEntity.ok(relatorio);
    }

    @GetMapping("/estatisticas/total")
    public ResponseEntity<Long> contarTreinos() {
        long total = treinoService.contarTreinos();
        return ResponseEntity.ok(total);
    }

    @GetMapping("/estatisticas/finalizados")
    public ResponseEntity<Long> contarTreinosFinalizados() {
        long total = treinoService.contarTreinosFinalizados();
        return ResponseEntity.ok(total);
    }
}
