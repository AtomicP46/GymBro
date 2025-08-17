package br.ifg.gymbro.services;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import br.ifg.gymbro.dto.PlanoExercicioDTO;
import br.ifg.gymbro.model.Exercicios;
import br.ifg.gymbro.model.PlanoExercicio;
import br.ifg.gymbro.repository.ExerciciosRepository;
import br.ifg.gymbro.repository.PlanoExercicioRepository;

public class PlanoExercicioService {
    private final PlanoExercicioRepository planoExercicioRepository;
    private final ExerciciosRepository exerciciosRepository;

    public PlanoExercicioService(PlanoExercicioRepository planoExercicioRepository,
                                 ExerciciosRepository exerciciosRepository) {
        this.planoExercicioRepository = planoExercicioRepository;
        this.exerciciosRepository = exerciciosRepository;
    }

    public PlanoExercicio adicionarExercicio(PlanoExercicioDTO dto) throws SQLException {
        // Validações
        if (dto.getPlanoId() == null) {
            throw new IllegalArgumentException("ID do plano é obrigatório");
        }

        if (dto.getExercicioId() == null) {
            throw new IllegalArgumentException("ID do exercício é obrigatório");
        }

        // Verificar se o exercício existe
        Optional<Exercicios> exercicioOpt = exerciciosRepository.buscarPorId(dto.getExercicioId());
        if (exercicioOpt.isEmpty()) {
            throw new IllegalArgumentException("Exercício não encontrado");
        }

        // Verificar se o exercício já está no plano
        if (planoExercicioRepository.existeExercicioNoPlano(dto.getPlanoId(), dto.getExercicioId())) {
            throw new IllegalArgumentException("Este exercício já está adicionado ao plano");
        }

        // Validar séries sugeridas
        if (dto.getSeriesSugeridas() != null && dto.getSeriesSugeridas() <= 0) {
            throw new IllegalArgumentException("Número de séries deve ser maior que zero");
        }

        PlanoExercicio planoExercicio = new PlanoExercicio(
            dto.getPlanoId(),
            dto.getExercicioId(),
            dto.getSeriesSugeridas(),
            dto.getObservacoes(),
            dto.getAquecimento() != null ? dto.getAquecimento() : false
        );

        return planoExercicioRepository.criar(planoExercicio);
    }

    public PlanoExercicio buscarPorId(Long id) throws SQLException {
        if (id == null) {
            throw new IllegalArgumentException("ID é obrigatório");
        }
        return planoExercicioRepository.buscarPorId(id);
    }

    public List<PlanoExercicio> listarPorPlano(Long planoId) throws SQLException {
        if (planoId == null) {
            throw new IllegalArgumentException("ID do plano é obrigatório");
        }
        return planoExercicioRepository.listarPorPlano(planoId);
    }

    public List<PlanoExercicio> listarAquecimentoPorPlano(Long planoId) throws SQLException {
        if (planoId == null) {
            throw new IllegalArgumentException("ID do plano é obrigatório");
        }
        return planoExercicioRepository.listarAquecimentoPorPlano(planoId);
    }

    public List<PlanoExercicio> listarPrincipaisPorPlano(Long planoId) throws SQLException {
        if (planoId == null) {
            throw new IllegalArgumentException("ID do plano é obrigatório");
        }
        return planoExercicioRepository.listarPrincipaisPorPlano(planoId);
    }

    public PlanoExercicio atualizarExercicio(Long id, PlanoExercicioDTO dto) throws SQLException {
        if (id == null) {
            throw new IllegalArgumentException("ID é obrigatório");
        }

        PlanoExercicio existente = planoExercicioRepository.buscarPorId(id);
        if (existente == null) {
            throw new IllegalArgumentException("Exercício do plano não encontrado");
        }

        // Validar séries sugeridas
        if (dto.getSeriesSugeridas() != null && dto.getSeriesSugeridas() <= 0) {
            throw new IllegalArgumentException("Número de séries deve ser maior que zero");
        }

        existente.setSeriesSugeridas(dto.getSeriesSugeridas());
        existente.setObservacoes(dto.getObservacoes());
        existente.setAquecimento(dto.getAquecimento() != null ? dto.getAquecimento() : false);

        return planoExercicioRepository.atualizar(existente);
    }

    public boolean removerExercicio(Long id) throws SQLException {
        if (id == null) {
            throw new IllegalArgumentException("ID é obrigatório");
        }

        PlanoExercicio existente = planoExercicioRepository.buscarPorId(id);
        if (existente == null) {
            throw new IllegalArgumentException("Exercício do plano não encontrado");
        }

        return planoExercicioRepository.excluir(id);
    }

    public boolean removerTodosExerciciosDoPlano(Long planoId) throws SQLException {
        if (planoId == null) {
            throw new IllegalArgumentException("ID do plano é obrigatório");
        }
        return planoExercicioRepository.excluirPorPlano(planoId);
    }
}
