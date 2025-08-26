package com.gymbro.service;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gymbro.dto.ExercicioDTO;
import com.gymbro.enums.RegiaoCorpo;
import com.gymbro.enums.TipoExercicio;
import com.gymbro.model.Equipamento;
import com.gymbro.model.Exercicio;
import com.gymbro.repository.EquipamentoRepository;
import com.gymbro.repository.ExercicioRepository;

@Service
public class ExercicioService {

    private final ExercicioRepository exercicioRepository;
    private final EquipamentoRepository equipamentoRepository;

    @Autowired
    public ExercicioService(ExercicioRepository exercicioRepository,
                             EquipamentoRepository equipamentoRepository) {
        this.exercicioRepository = exercicioRepository;
        this.equipamentoRepository = equipamentoRepository;
    }

    @Transactional
    public Exercicio criarExercicio(ExercicioDTO dto) {
        validarDadosObrigatorios(dto);
        validarNomeUnico(dto.getNome().trim());
        
        return Optional.of(dto)
                .map(this::construirExercicio)
                .map(exercicioRepository::save)
                .orElseThrow(() -> new IllegalStateException("Erro ao criar exercício"));
    }

    @Transactional(readOnly = true)
    public Exercicio buscarPorId(Long id) {
        return Optional.ofNullable(id)
                .filter(i -> i > 0)
                .flatMap(exercicioRepository::findById)
                .orElseThrow(() -> new IllegalArgumentException(
                    id == null || id <= 0 ? "ID deve ser positivo" : "Exercício não encontrado"));
    }

    @Transactional(readOnly = true)
    public List<Exercicio> listarTodos() {
        return exercicioRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Exercicio> buscarPorNome(String nome) {
        return Optional.ofNullable(nome)
                .filter(n -> !n.trim().isEmpty())
                .map(String::trim)
                .map(exercicioRepository::findByNomeContainingIgnoreCaseOrderByNome)
                .orElseThrow(() -> new IllegalArgumentException("Nome para busca não pode estar vazio"));
    }

    @Transactional(readOnly = true)
    public List<Exercicio> buscarPorRegiao(String regiao) {
        return Optional.ofNullable(regiao)
                .filter(r -> !r.trim().isEmpty())
                .map(String::trim)
                .map(RegiaoCorpo::fromString)
                .map(exercicioRepository::findByRegiaoOrderByNome)
                .orElseThrow(() -> new IllegalArgumentException("Região para busca não pode estar vazia"));
    }

    @Transactional(readOnly = true)
    public List<Exercicio> buscarPorTipo(String tipo) {
        return Optional.ofNullable(tipo)
                .filter(t -> !t.trim().isEmpty())
                .map(String::trim)
                .map(TipoExercicio::fromString)
                .map(exercicioRepository::findByTipoOrderByNome)
                .orElseThrow(() -> new IllegalArgumentException("Tipo para busca não pode estar vazio"));
    }

    @Transactional(readOnly = true)
    public List<Exercicio> buscarPorEquipamento(Long equipamentoId) {
        return Optional.ofNullable(equipamentoId)
                .filter(id -> id > 0)
                .map(id -> {
                    equipamentoRepository.findById(id)
                        .orElseThrow(() -> new IllegalArgumentException("Equipamento não encontrado"));
                    return exercicioRepository.findByEquipamentoIdOrderByNome(id);
                })
                .orElseThrow(() -> new IllegalArgumentException("ID do equipamento deve ser positivo"));
    }

    @Transactional(readOnly = true)
    public List<Exercicio> buscarPorUnilateral(Boolean unilateral) {
        return Optional.ofNullable(unilateral)
                .map(exercicioRepository::findByUnilateralOrderByNome)
                .orElseThrow(() -> new IllegalArgumentException("Parâmetro unilateral é obrigatório"));
    }

    @Transactional(readOnly = true)
    public List<Exercicio> buscarPorCriterio(Predicate<Exercicio> criterio) {
        return exercicioRepository.findAll().stream()
                .filter(criterio)
                .collect(Collectors.toList());
    }

    @Transactional
    public Exercicio atualizarExercicio(Long id, ExercicioDTO dto) {
        return Optional.of(buscarPorId(id))
                .map(exercicio -> atualizarDadosExercicio(exercicio, dto))
                .map(exercicioRepository::save)
                .orElseThrow(() -> new IllegalStateException("Erro ao atualizar exercício"));
    }

    @Transactional
    public void deletarExercicio(Long id) {
        if (!exercicioRepository.existsById(id)) {
            throw new IllegalArgumentException("Exercício não encontrado");
        }
        exercicioRepository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public long contarExercicio() {
        return exercicioRepository.count();
    }

    @Transactional(readOnly = true)
    public long contarPorRegiao(String regiao) {
        return Optional.ofNullable(regiao)
                .filter(r -> !r.trim().isEmpty())
                .map(String::trim)
                .map(RegiaoCorpo::fromString)
                .map(exercicioRepository::countByRegiao)
                .orElseThrow(() -> new IllegalArgumentException("Região não pode estar vazia"));
    }

    @Transactional(readOnly = true)
    public boolean existeExercicio(Long id) {
        return Optional.ofNullable(id)
                .filter(i -> i > 0)
                .map(exercicioRepository::existsById)
                .orElse(false);
    }

    public String[] getRegioesValidas() {
        return RegiaoCorpo.getDescricoes();
    }

    public String[] getTiposValidos() {
        return TipoExercicio.getDescricoes();
    }

    private void validarDadosObrigatorios(ExercicioDTO dto) {
        if (dto.getNome() == null || dto.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do exercício é obrigatório");
        }
        if (dto.getRegiao() == null || dto.getRegiao().trim().isEmpty()) {
            throw new IllegalArgumentException("Região do exercício é obrigatória");
        }
        if (dto.getTipo() == null || dto.getTipo().trim().isEmpty()) {
            throw new IllegalArgumentException("Tipo do exercício é obrigatório");
        }
        if (dto.getUnilateral() == null) {
            throw new IllegalArgumentException("Indicação de unilateralidade é obrigatória");
        }
    }

    private void validarNomeUnico(String nome) {
        if (exercicioRepository.existsByNomeIgnoreCase(nome)) {
            throw new IllegalArgumentException("Já existe um exercício com este nome");
        }
    }

    private Exercicio construirExercicio(ExercicioDTO dto) {
        RegiaoCorpo regiaoEnum = RegiaoCorpo.fromString(dto.getRegiao().trim());
        TipoExercicio tipoEnum = TipoExercicio.fromString(dto.getTipo().trim());
        
        Equipamento equipamento = Optional.ofNullable(dto.getEquipamentoId())
                .flatMap(equipamentoRepository::findById)
                .orElse(null);

        return new Exercicio(dto.getNome().trim(), regiaoEnum, tipoEnum, dto.getUnilateral(), equipamento);
    }

    private Exercicio atualizarDadosExercicio(Exercicio exercicio, ExercicioDTO dto) {
        // Atualiza nome
        Optional.ofNullable(dto.getNome())
                .filter(nome -> !nome.trim().isEmpty())
                .map(String::trim)
                .ifPresent(nome -> {
                    if (exercicioRepository.existsByNomeIgnoreCaseAndIdNot(nome, exercicio.getId())) {
                        throw new IllegalArgumentException("Já existe outro exercício com este nome");
                    }
                    exercicio.setNome(nome);
                });

        // Atualiza região
        Optional.ofNullable(dto.getRegiao())
                .filter(regiao -> !regiao.trim().isEmpty())
                .map(String::trim)
                .map(RegiaoCorpo::fromString)
                .ifPresent(exercicio::setRegiao);

        // Atualiza tipo
        Optional.ofNullable(dto.getTipo())
                .filter(tipo -> !tipo.trim().isEmpty())
                .map(String::trim)
                .map(TipoExercicio::fromString)
                .ifPresent(exercicio::setTipo);

        // Atualiza unilateral
        Optional.ofNullable(dto.getUnilateral())
                .ifPresent(exercicio::setUnilateral);

        // Atualiza equipamento
        Optional.ofNullable(dto.getEquipamentoId())
                .ifPresent(equipId -> {
                    if (equipId > 0) {
                        Equipamento equip = equipamentoRepository.findById(equipId)
                            .orElseThrow(() -> new IllegalArgumentException("Equipamento não encontrado"));
                        exercicio.setEquipamento(equip);
                    } else {
                        exercicio.setEquipamento(null);
                    }
                });

        return exercicio;
    }
}
