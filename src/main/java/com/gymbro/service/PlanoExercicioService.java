package com.gymbro.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import com.gymbro.dto.PlanoExercicioDTO;
import com.gymbro.model.PlanoExercicio;
import com.gymbro.repository.ExercicioRepository;
import com.gymbro.repository.PlanoExercicioRepository;
import com.gymbro.exception.ResourceNotFoundException;
import com.gymbro.exception.BusinessException;

@Service
@Validated
@Transactional
public class PlanoExercicioService {
    
    @Autowired
    private PlanoExercicioRepository planoExercicioRepository;
    
    @Autowired
    private ExercicioRepository exercicioRepository;

    // Os predicados serão definidos como métodos privados para evitar problemas de inicialização

    @Transactional
    public PlanoExercicio adicionarExercicio(@Valid PlanoExercicioDTO dto) {
        if (!exercicioExiste(dto.getExercicioId())) {
            throw new ResourceNotFoundException("Exercício não encontrado com ID: " + dto.getExercicioId());
        }
        
        if (exercicioJaExisteNoPlano(dto)) {
            throw new BusinessException("Este exercício já está adicionado ao plano");
        }
        
        PlanoExercicio planoExercicio = criarPlanoExercicio(dto);
        return planoExercicioRepository.save(planoExercicio);
    }

    private boolean exercicioExiste(Long exercicioId) {
        return exercicioRepository.existsById(exercicioId);
    }

    private boolean exercicioJaExisteNoPlano(PlanoExercicioDTO dto) {
        return planoExercicioRepository.existsByPlanoIdAndExercicioId(dto.getPlanoId(), dto.getExercicioId());
    }

    private PlanoExercicio criarPlanoExercicio(PlanoExercicioDTO dto) {
        return new PlanoExercicio(
            dto.getPlanoId(),
            dto.getExercicioId(),
            dto.getSeriesSugeridas(),
            dto.getObservacoes(),
            Optional.ofNullable(dto.getAquecimento()).orElse(false)
        );
    }

    @Transactional(readOnly = true)
    public Optional<PlanoExercicio> buscarPorId(@NotNull Long id) {
        return planoExercicioRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public PlanoExercicio obterPorId(@NotNull Long id) {
        return buscarPorId(id)
            .orElseThrow(() -> new ResourceNotFoundException("Exercício do plano não encontrado com ID: " + id));
    }

    @Transactional(readOnly = true)
    public List<PlanoExercicio> listarPorPlano(@NotNull Long planoId) {
        return planoExercicioRepository.findByPlanoIdOrderByAquecimentoDescId(planoId);
    }

    @Transactional(readOnly = true)
    public List<PlanoExercicio> listarAquecimentoPorPlano(@NotNull Long planoId) {
        return listarPorPlano(planoId).stream()
            .filter(PlanoExercicio::getAquecimento)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PlanoExercicio> listarPrincipaisPorPlano(@NotNull Long planoId) {
        return listarPorPlano(planoId).stream()
            .filter(pe -> !pe.getAquecimento())
            .collect(Collectors.toList());
    }

    @Transactional
    public PlanoExercicio atualizarExercicio(@NotNull Long id, @Valid PlanoExercicioDTO dto) {
        return buscarPorId(id)
            .map(existente -> atualizarCampos(existente, dto))
            .map(planoExercicioRepository::save)
            .orElseThrow(() -> new ResourceNotFoundException("Exercício do plano não encontrado com ID: " + id));
    }

    private PlanoExercicio atualizarCampos(PlanoExercicio existente, PlanoExercicioDTO dto) {
        existente.setSeriesSugeridas(dto.getSeriesSugeridas());
        existente.setObservacoes(dto.getObservacoes());
        existente.setAquecimento(Optional.ofNullable(dto.getAquecimento()).orElse(false));
        return existente;
    }

    @Transactional
    public void removerExercicio(@NotNull Long id) {
        buscarPorId(id)
            .ifPresentOrElse(
                pe -> planoExercicioRepository.deleteById(id),
                () -> { throw new ResourceNotFoundException("Exercício do plano não encontrado com ID: " + id); }
            );
    }

    @Transactional
    public void removerTodosExerciciosDoPlano(@NotNull Long planoId) {
        planoExercicioRepository.deleteByPlanoId(planoId);
    }

    @Transactional(readOnly = true)
    public long contarExerciciosPorPlano(@NotNull Long planoId) {
        return listarPorPlano(planoId).stream().count();
    }

    @Transactional(readOnly = true)
    public long contarAquecimentosPorPlano(@NotNull Long planoId) {
        return listarPorPlano(planoId).stream()
            .filter(PlanoExercicio::getAquecimento)
            .count();
    }
}
