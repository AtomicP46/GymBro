package com.gymbro.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.util.List;

import com.gymbro.dto.PlanoExercicioDTO;
import com.gymbro.model.Exercicio;
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

    @Transactional
    public PlanoExercicio adicionarExercicio(@Valid PlanoExercicioDTO dto) {
        // Verificar se o exercício existe
        if (!exercicioRepository.existsById(dto.getExercicioId())) {
            throw new ResourceNotFoundException("Exercício não encontrado com ID: " + dto.getExercicioId());
        }

        // Verificar se o exercício já está no plano
        if (planoExercicioRepository.existsByPlanoIdAndExercicioId(dto.getPlanoId(), dto.getExercicioId())) {
            throw new BusinessException("Este exercício já está adicionado ao plano");
        }

        PlanoExercicio planoExercicio = new PlanoExercicio(
            dto.getPlanoId(),
            dto.getExercicioId(),
            dto.getSeriesSugeridas(),
            dto.getObservacoes(),
            dto.getAquecimento() != null ? dto.getAquecimento() : false
        );

        return planoExercicioRepository.save(planoExercicio);
    }

    @Transactional(readOnly = true)
    public PlanoExercicio buscarPorId(@NotNull Long id) {
        return planoExercicioRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Exercício do plano não encontrado com ID: " + id));
    }

    @Transactional(readOnly = true)
    public List<PlanoExercicio> listarPorPlano(@NotNull Long planoId) {
        return planoExercicioRepository.findByPlanoIdOrderByAquecimentoDescId(planoId);
    }

    @Transactional(readOnly = true)
    public List<PlanoExercicio> listarAquecimentoPorPlano(@NotNull Long planoId) {
        return planoExercicioRepository.findByPlanoIdAndAquecimentoTrueOrderById(planoId);
    }

    @Transactional(readOnly = true)
    public List<PlanoExercicio> listarPrincipaisPorPlano(@NotNull Long planoId) {
        return planoExercicioRepository.findByPlanoIdAndAquecimentoFalseOrderById(planoId);
    }

    @Transactional
    public PlanoExercicio atualizarExercicio(@NotNull Long id, @Valid PlanoExercicioDTO dto) {
        PlanoExercicio existente = buscarPorId(id);

        existente.setSeriesSugeridas(dto.getSeriesSugeridas());
        existente.setObservacoes(dto.getObservacoes());
        existente.setAquecimento(dto.getAquecimento() != null ? dto.getAquecimento() : false);

        return planoExercicioRepository.save(existente);
    }

    @Transactional
    public void removerExercicio(@NotNull Long id) {
        if (!planoExercicioRepository.existsById(id)) {
            throw new ResourceNotFoundException("Exercício do plano não encontrado com ID: " + id);
        }
        planoExercicioRepository.deleteById(id);
    }

    @Transactional
    public void removerTodosExerciciosDoPlano(@NotNull Long planoId) {
        planoExercicioRepository.deleteByPlanoId(planoId);
    }
}
