package br.ifg.gymbro.services;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import br.ifg.gymbro.dto.TreinoExercicioDTO;
import br.ifg.gymbro.model.TreinoExercicio;
import br.ifg.gymbro.repository.ExerciciosRepository;
import br.ifg.gymbro.repository.TreinoExercicioRepository;
import br.ifg.gymbro.repository.TreinoRepository;

public class TreinoExercicioService {
    private TreinoExercicioRepository treinoExercicioRepository;
    private TreinoRepository treinoRepository;
    private ExerciciosRepository exerciciosRepository;

    public TreinoExercicioService(TreinoExercicioRepository treinoExercicioRepository,
                                 TreinoRepository treinoRepository,
                                 ExerciciosRepository exerciciosRepository) {
        this.treinoExercicioRepository = treinoExercicioRepository;
        this.treinoRepository = treinoRepository;
        this.exerciciosRepository = exerciciosRepository;
    }

    public TreinoExercicio adicionarExercicioAoTreino(TreinoExercicioDTO treinoExercicioDTO) 
            throws SQLException, IllegalArgumentException {
        
        // Validações
        if (treinoExercicioDTO.getTreinoId() == null) {
            throw new IllegalArgumentException("ID do treino é obrigatório");
        }

        if (treinoExercicioDTO.getExercicioId() == null) {
            throw new IllegalArgumentException("ID do exercício é obrigatório");
        }

        // Verificar se treino existe
        if (treinoRepository.buscarPorId(treinoExercicioDTO.getTreinoId()).isEmpty()) {
            throw new IllegalArgumentException("Treino não encontrado");
        }

        // Verificar se exercício existe
        if (exerciciosRepository.buscarPorId(treinoExercicioDTO.getExercicioId()).isEmpty()) {
            throw new IllegalArgumentException("Exercício não encontrado");
        }

        // Verificar se exercício já está no treino
        if (treinoExercicioRepository.existeExercicioNoTreino(
                treinoExercicioDTO.getTreinoId(), treinoExercicioDTO.getExercicioId())) {
            throw new IllegalArgumentException("Exercício já está adicionado a este treino");
        }

        // Validar valores numéricos
        if (treinoExercicioDTO.getSeries() != null && treinoExercicioDTO.getSeries() <= 0) {
            throw new IllegalArgumentException("Número de séries deve ser positivo");
        }

        if (treinoExercicioDTO.getRepeticoes() != null && treinoExercicioDTO.getRepeticoes() <= 0) {
            throw new IllegalArgumentException("Número de repetições deve ser positivo");
        }

        if (treinoExercicioDTO.getPesoUsado() != null && treinoExercicioDTO.getPesoUsado() < 0) {
            throw new IllegalArgumentException("Peso usado não pode ser negativo");
        }

        // Criar relação treino-exercício
        TreinoExercicio treinoExercicio = new TreinoExercicio(
            treinoExercicioDTO.getTreinoId(),
            treinoExercicioDTO.getExercicioId(),
            treinoExercicioDTO.getSeries(),
            treinoExercicioDTO.getRepeticoes(),
            treinoExercicioDTO.getPesoUsado(),
            treinoExercicioDTO.getAnotacoes(),
            treinoExercicioDTO.getAquecimento() != null ? treinoExercicioDTO.getAquecimento() : false
        );

        return treinoExercicioRepository.salvar(treinoExercicio);
    }

    public Optional<TreinoExercicio> buscarTreinoExercicioPorId(Long id) throws SQLException {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID deve ser um número positivo");
        }
        return treinoExercicioRepository.buscarPorId(id);
    }

    public List<TreinoExercicio> listarExerciciosDoTreino(Long treinoId) throws SQLException {
        if (treinoId == null || treinoId <= 0) {
            throw new IllegalArgumentException("ID do treino deve ser um número positivo");
        }

        // Verificar se treino existe
        if (treinoRepository.buscarPorId(treinoId).isEmpty()) {
            throw new IllegalArgumentException("Treino não encontrado");
        }

        return treinoExercicioRepository.buscarPorTreino(treinoId);
    }

    public TreinoExercicio atualizarExercicioDoTreino(Long id, TreinoExercicioDTO treinoExercicioDTO) 
            throws SQLException, IllegalArgumentException {
        
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID deve ser um número positivo");
        }

        Optional<TreinoExercicio> treinoExercicioOpt = treinoExercicioRepository.buscarPorId(id);
        
        if (treinoExercicioOpt.isEmpty()) {
            throw new IllegalArgumentException("Exercício do treino não encontrado");
        }

        TreinoExercicio treinoExercicio = treinoExercicioOpt.get();
        
        // Atualizar séries
        if (treinoExercicioDTO.getSeries() != null) {
            if (treinoExercicioDTO.getSeries() <= 0) {
                throw new IllegalArgumentException("Número de séries deve ser positivo");
            }
            treinoExercicio.setSeries(treinoExercicioDTO.getSeries());
        }
        
        // Atualizar repetições
        if (treinoExercicioDTO.getRepeticoes() != null) {
            if (treinoExercicioDTO.getRepeticoes() <= 0) {
                throw new IllegalArgumentException("Número de repetições deve ser positivo");
            }
            treinoExercicio.setRepeticoes(treinoExercicioDTO.getRepeticoes());
        }

        // Atualizar peso usado
        if (treinoExercicioDTO.getPesoUsado() != null) {
            if (treinoExercicioDTO.getPesoUsado() < 0) {
                throw new IllegalArgumentException("Peso usado não pode ser negativo");
            }
            treinoExercicio.setPesoUsado(treinoExercicioDTO.getPesoUsado());
        }

        // Atualizar anotações
        if (treinoExercicioDTO.getAnotacoes() != null) {
            treinoExercicio.setAnotacoes(treinoExercicioDTO.getAnotacoes());
        }

        // Atualizar aquecimento
        if (treinoExercicioDTO.getAquecimento() != null) {
            treinoExercicio.setAquecimento(treinoExercicioDTO.getAquecimento());
        }

        treinoExercicioRepository.atualizar(treinoExercicio);
        return treinoExercicioRepository.buscarPorId(id).orElse(treinoExercicio);
    }

    public void removerExercicioDoTreino(Long id) throws SQLException, IllegalArgumentException {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID deve ser um número positivo");
        }

        Optional<TreinoExercicio> treinoExercicioOpt = treinoExercicioRepository.buscarPorId(id);
        
        if (treinoExercicioOpt.isEmpty()) {
            throw new IllegalArgumentException("Exercício do treino não encontrado");
        }

        treinoExercicioRepository.deletar(id);
    }

    public void removerTodosExerciciosDoTreino(Long treinoId) throws SQLException, IllegalArgumentException {
        if (treinoId == null || treinoId <= 0) {
            throw new IllegalArgumentException("ID do treino deve ser um número positivo");
        }

        // Verificar se treino existe
        if (treinoRepository.buscarPorId(treinoId).isEmpty()) {
            throw new IllegalArgumentException("Treino não encontrado");
        }

        treinoExercicioRepository.deletarPorTreino(treinoId);
    }

    public boolean exercicioExisteNoTreino(Long treinoId, Long exercicioId) throws SQLException {
        if (treinoId == null || treinoId <= 0 || exercicioId == null || exercicioId <= 0) {
            return false;
        }
        return treinoExercicioRepository.existeExercicioNoTreino(treinoId, exercicioId);
    }

    public TreinoExercicio registrarExecucao(Long id, Integer repeticoes, Float pesoUsado, String anotacoes) 
            throws SQLException, IllegalArgumentException {
        
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID deve ser um número positivo");
        }

        Optional<TreinoExercicio> treinoExercicioOpt = treinoExercicioRepository.buscarPorId(id);
        
        if (treinoExercicioOpt.isEmpty()) {
            throw new IllegalArgumentException("Exercício do treino não encontrado");
        }

        TreinoExercicio treinoExercicio = treinoExercicioOpt.get();

        // Validar valores
        if (repeticoes != null && repeticoes <= 0) {
            throw new IllegalArgumentException("Número de repetições deve ser positivo");
        }

        if (pesoUsado != null && pesoUsado < 0) {
            throw new IllegalArgumentException("Peso usado não pode ser negativo");
        }

        // Atualizar dados de execução
        if (repeticoes != null) {
            treinoExercicio.setRepeticoes(repeticoes);
        }
        
        if (pesoUsado != null) {
            treinoExercicio.setPesoUsado(pesoUsado);
        }
        
        if (anotacoes != null) {
            treinoExercicio.setAnotacoes(anotacoes);
        }

        treinoExercicioRepository.atualizar(treinoExercicio);
        return treinoExercicioRepository.buscarPorId(id).orElse(treinoExercicio);
    }
}
