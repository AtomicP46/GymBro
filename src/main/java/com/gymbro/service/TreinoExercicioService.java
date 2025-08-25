package com.gymbro.service;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gymbro.dto.ExercicioProgressoDTO;
import com.gymbro.dto.TreinoExercicioDTO;
import com.gymbro.model.TreinoExercicio;
import com.gymbro.repository.ExercicioRepository;
import com.gymbro.repository.TreinoExercicioRepository;
import com.gymbro.repository.TreinoRepository;

@Service
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

    public TreinoExercicio adicionarExercicioAoTreino(TreinoExercicioDTO treinoExercicioDTO) {
        if (!treinoRepository.existsById(treinoExercicioDTO.getTreinoId())) {
            throw new IllegalArgumentException("Treino não encontrado");
        }

        if (!exercicioRepository.existsById(treinoExercicioDTO.getExercicioId())) {
            throw new IllegalArgumentException("Exercício não encontrado");
        }

        if (treinoExercicioRepository.existsByTreinoIdAndExercicioId(
                treinoExercicioDTO.getTreinoId(), treinoExercicioDTO.getExercicioId())) {
            throw new IllegalArgumentException("Exercício já está adicionado a este treino");
        }

        TreinoExercicio treinoExercicio = modelMapper.map(treinoExercicioDTO, TreinoExercicio.class);
        if (treinoExercicio.getAquecimento() == null) {
            treinoExercicio.setAquecimento(false);
        }

        return treinoExercicioRepository.save(treinoExercicio);
    }

    @Transactional(readOnly = true)
    public Optional<TreinoExercicio> buscarTreinoExercicioPorId(Long id) {
        return treinoExercicioRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public List<TreinoExercicio> listarExerciciosDoTreino(Long treinoId) {
        if (!treinoRepository.existsById(treinoId)) {
            throw new IllegalArgumentException("Treino não encontrado");
        }
        return treinoExercicioRepository.findByTreinoIdOrderById(treinoId);
    }

    public TreinoExercicio atualizarExercicioDoTreino(Long id, TreinoExercicioDTO treinoExercicioDTO) {
        TreinoExercicio treinoExercicio = treinoExercicioRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Exercício do treino não encontrado"));

        modelMapper.map(treinoExercicioDTO, treinoExercicio);
        return treinoExercicioRepository.save(treinoExercicio);
    }

    public void removerExercicioDoTreino(Long id) {
        if (!treinoExercicioRepository.existsById(id)) {
            throw new IllegalArgumentException("Exercício do treino não encontrado");
        }
        treinoExercicioRepository.deleteById(id);
    }

    public void removerTodosExerciciosDoTreino(Long treinoId) {
        if (!treinoRepository.existsById(treinoId)) {
            throw new IllegalArgumentException("Treino não encontrado");
        }
        treinoExercicioRepository.deleteByTreinoId(treinoId);
    }

    @Transactional(readOnly = true)
    public boolean exercicioExisteNoTreino(Long treinoId, Long exercicioId) {
        return treinoExercicioRepository.existsByTreinoIdAndExercicioId(treinoId, exercicioId);
    }

    public TreinoExercicio registrarExecucao(Long id, Integer repeticoes, Float pesoUsado, String anotacoes) {
        TreinoExercicio treinoExercicio = treinoExercicioRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Exercício do treino não encontrado"));

        if (repeticoes != null) {
            treinoExercicio.setRepeticoes(repeticoes);
        }
        if (pesoUsado != null) {
            treinoExercicio.setPesoUsado(pesoUsado);
        }
        if (anotacoes != null) {
            treinoExercicio.setAnotacoes(anotacoes);
        }

        return treinoExercicioRepository.save(treinoExercicio);
    }

    @Transactional(readOnly = true)
    public List<ExercicioProgressoDTO> gerarRelatorioProgressoExercicio(Long usuarioId, Long exercicioId) {
        return treinoExercicioRepository.findHistoricoExercicioPorUsuario(usuarioId, exercicioId);
    }
}
