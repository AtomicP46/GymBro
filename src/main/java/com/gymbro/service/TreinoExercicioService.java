package com.gymbro.service;

import com.gymbro.dto.ExercicioProgressoDTO;
import com.gymbro.dto.TreinoExercicioDTO;
import com.gymbro.model.TreinoExercicio;
import com.gymbro.repository.ExercicioRepository;
import com.gymbro.repository.TreinoExercicioRepository;
import com.gymbro.repository.TreinoRepository;
import com.gymbro.exception.ResourceNotFoundException;
import com.gymbro.exception.BusinessException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Validated
@Transactional
public class TreinoExercicioService {
    
    @Autowired
    private TreinoExercicioRepository treinoExercicioRepository;
    
    @Autowired
    private TreinoRepository treinoRepository;
    
    @Autowired
    private ExercicioRepository exercicioRepository;
    
    @Autowired
    private ModelMapper modelMapper;

    @Transactional
    public TreinoExercicio adicionarExercicioAoTreino(@Valid TreinoExercicioDTO treinoExercicioDTO) {
        if (!treinoExiste(treinoExercicioDTO.getTreinoId())) {
            throw new ResourceNotFoundException("Treino não encontrado");
        }
        
        if (!exercicioExiste(treinoExercicioDTO.getExercicioId())) {
            throw new ResourceNotFoundException("Exercício não encontrado");
        }
        
        if (exercicioJaExisteNoTreino(treinoExercicioDTO)) {
            throw new BusinessException("Exercício já está adicionado a este treino");
        }
        
        TreinoExercicio treinoExercicio = criarTreinoExercicio(treinoExercicioDTO);
        return treinoExercicioRepository.save(treinoExercicio);
    }

    private boolean treinoExiste(Long treinoId) {
        return treinoRepository.existsById(treinoId);
    }

    private boolean exercicioExiste(Long exercicioId) {
        return exercicioRepository.existsById(exercicioId);
    }

    private boolean exercicioJaExisteNoTreino(TreinoExercicioDTO dto) {
        return treinoExercicioRepository.existsByTreinoIdAndExercicioId(dto.getTreinoId(), dto.getExercicioId());
    }

    private TreinoExercicio criarTreinoExercicio(TreinoExercicioDTO dto) {
        TreinoExercicio treinoExercicio = modelMapper.map(dto, TreinoExercicio.class);
        treinoExercicio.setAquecimento(Optional.ofNullable(dto.getAquecimento()).orElse(false));
        return treinoExercicio;
    }

    @Transactional(readOnly = true)
    public Optional<TreinoExercicio> buscarTreinoExercicioPorId(@NotNull Long id) {
        return treinoExercicioRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public TreinoExercicio obterTreinoExercicioPorId(@NotNull Long id) {
        return buscarTreinoExercicioPorId(id)
            .orElseThrow(() -> new ResourceNotFoundException("Exercício do treino não encontrado"));
    }

    @Transactional(readOnly = true)
    public List<TreinoExercicio> listarExerciciosDoTreino(@NotNull Long treinoId) {
        return Optional.of(treinoId)
            .filter(this::treinoExiste)
            .map(id -> treinoExercicioRepository.findByTreinoIdOrderById(id))
            .orElseThrow(() -> new ResourceNotFoundException("Treino não encontrado"));
    }

    @Transactional
    public TreinoExercicio atualizarExercicioDoTreino(@NotNull Long id, @Valid TreinoExercicioDTO treinoExercicioDTO) {
        return buscarTreinoExercicioPorId(id)
            .map(existente -> {
                modelMapper.map(treinoExercicioDTO, existente);
                return existente;
            })
            .map(treinoExercicioRepository::save)
            .orElseThrow(() -> new ResourceNotFoundException("Exercício do treino não encontrado"));
    }

    @Transactional
    public void removerExercicioDoTreino(@NotNull Long id) {
        buscarTreinoExercicioPorId(id)
            .ifPresentOrElse(
                te -> treinoExercicioRepository.deleteById(id),
                () -> { throw new ResourceNotFoundException("Exercício do treino não encontrado"); }
            );
    }

    @Transactional
    public void removerTodosExerciciosDoTreino(@NotNull Long treinoId) {
        Optional.of(treinoId)
            .filter(this::treinoExiste)
            .ifPresentOrElse(
                id -> treinoExercicioRepository.deleteByTreinoId(id),
                () -> { throw new ResourceNotFoundException("Treino não encontrado"); }
            );
    }

    @Transactional(readOnly = true)
    public boolean exercicioExisteNoTreino(@NotNull Long treinoId, @NotNull Long exercicioId) {
        return treinoExercicioRepository.existsByTreinoIdAndExercicioId(treinoId, exercicioId);
    }

    @Transactional
    public TreinoExercicio registrarExecucao(@NotNull Long id, Integer repeticoes, Float pesoUsado, String anotacoes) {
        return buscarTreinoExercicioPorId(id)
            .map(te -> atualizarExecucao(te, repeticoes, pesoUsado, anotacoes))
            .map(treinoExercicioRepository::save)
            .orElseThrow(() -> new ResourceNotFoundException("Exercício do treino não encontrado"));
    }

    private TreinoExercicio atualizarExecucao(TreinoExercicio te, Integer repeticoes, Float pesoUsado, String anotacoes) {
        Optional.ofNullable(repeticoes).ifPresent(te::setRepeticoes);
        Optional.ofNullable(pesoUsado).ifPresent(te::setPesoUsado);
        Optional.ofNullable(anotacoes).ifPresent(te::setAnotacoes);
        return te;
    }

    @Transactional(readOnly = true)
    public List<ExercicioProgressoDTO> gerarRelatorioProgressoExercicio(@NotNull Long usuarioId, @NotNull Long exercicioId) {
        return treinoExercicioRepository.findHistoricoExercicioPorUsuario(usuarioId, exercicioId);
    }

    @Transactional(readOnly = true)
    public List<TreinoExercicio> listarExerciciosAquecimento(@NotNull Long treinoId) {
        return listarExerciciosDoTreino(treinoId).stream()
            .filter(TreinoExercicio::getAquecimento)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<TreinoExercicio> listarExerciciosPrincipais(@NotNull Long treinoId) {
        return listarExerciciosDoTreino(treinoId).stream()
            .filter(te -> !te.getAquecimento())
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public long contarExerciciosPorTreino(@NotNull Long treinoId) {
        return listarExerciciosDoTreino(treinoId).stream().count();
    }
}
