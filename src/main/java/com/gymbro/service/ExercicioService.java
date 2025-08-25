package com.gymbro.service;

import java.util.List;

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
        // Validações básicas
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

        // Conversão para enums e validação
        RegiaoCorpo regiaoEnum = RegiaoCorpo.fromString(dto.getRegiao().trim());
        TipoExercicio tipoEnum = TipoExercicio.fromString(dto.getTipo().trim());

        // Verifica duplicidade de nome
        if (exercicioRepository.existsByNomeIgnoreCase(dto.getNome().trim())) {
            throw new IllegalArgumentException("Já existe um exercício com este nome");
        }

        // Busca e associa equipamento (opcional)
        Equipamento equipamento = null;
        if (dto.getEquipamentoId() != null) {
            equipamento = equipamentoRepository.findById(dto.getEquipamentoId())
                .orElseThrow(() -> new IllegalArgumentException("Equipamento não encontrado"));
        }

        Exercicio exercicio = new Exercicio(
            dto.getNome().trim(),
            regiaoEnum,
            tipoEnum,
            dto.getUnilateral(),
            equipamento
        );
        return exercicioRepository.save(exercicio);
    }

    @Transactional(readOnly = true)
    public Exercicio buscarPorId(Long id) {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID deve ser positivo");
        }
        return exercicioRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Exercício não encontrado"));
    }

    @Transactional(readOnly = true)
    public List<Exercicio> listarTodos() {
        return exercicioRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Exercicio> buscarPorNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome para busca não pode estar vazio");
        }
        return exercicioRepository.findByNomeContainingIgnoreCaseOrderByNome(nome.trim());
    }

    @Transactional(readOnly = true)
    public List<Exercicio> buscarPorRegiao(String regiao) {
        if (regiao == null || regiao.trim().isEmpty()) {
            throw new IllegalArgumentException("Região para busca não pode estar vazia");
        }
        RegiaoCorpo regiaoEnum = RegiaoCorpo.fromString(regiao.trim());
        return exercicioRepository.findByRegiaoOrderByNome(regiaoEnum);
    }

    @Transactional(readOnly = true)
    public List<Exercicio> buscarPorTipo(String tipo) {
        if (tipo == null || tipo.trim().isEmpty()) {
            throw new IllegalArgumentException("Tipo para busca não pode estar vazio");
        }
        TipoExercicio tipoEnum = TipoExercicio.fromString(tipo.trim());
        return exercicioRepository.findByTipoOrderByNome(tipoEnum);
    }

    @Transactional(readOnly = true)
    public List<Exercicio> buscarPorEquipamento(Long equipamentoId) {
        if (equipamentoId == null || equipamentoId <= 0) {
            throw new IllegalArgumentException("ID do equipamento deve ser positivo");
        }
        equipamentoRepository.findById(equipamentoId)
            .orElseThrow(() -> new IllegalArgumentException("Equipamento não encontrado"));
        return exercicioRepository.findByEquipamentoIdOrderByNome(equipamentoId);
    }

    @Transactional(readOnly = true)
    public List<Exercicio> buscarPorUnilateral(Boolean unilateral) {
        if (unilateral == null) {
            throw new IllegalArgumentException("Parâmetro unilateral é obrigatório");
        }
        return exercicioRepository.findByUnilateralOrderByNome(unilateral);
    }

    @Transactional
    public Exercicio atualizarExercicio(Long id, ExercicioDTO dto) {
        Exercicio exercicio = buscarPorId(id);

        // Atualiza nome
        if (dto.getNome() != null && !dto.getNome().trim().isEmpty()) {
            String novoNome = dto.getNome().trim();
            if (exercicioRepository.existsByNomeIgnoreCaseAndIdNot(novoNome, id)) {
                throw new IllegalArgumentException("Já existe outro exercício com este nome");
            }
            exercicio.setNome(novoNome);
        }

        // Atualiza região
        if (dto.getRegiao() != null && !dto.getRegiao().trim().isEmpty()) {
            RegiaoCorpo novaRegiao = RegiaoCorpo.fromString(dto.getRegiao().trim());
            exercicio.setRegiao(novaRegiao);
        }

        // Atualiza tipo
        if (dto.getTipo() != null && !dto.getTipo().trim().isEmpty()) {
            TipoExercicio novoTipo = TipoExercicio.fromString(dto.getTipo().trim());
            exercicio.setTipo(novoTipo);
        }

        // Atualiza unilateral
        if (dto.getUnilateral() != null) {
            exercicio.setUnilateral(dto.getUnilateral());
        }

        // Atualiza equipamento
        if (dto.getEquipamentoId() != null) {
            if (dto.getEquipamentoId() > 0) {
                Equipamento equip = equipamentoRepository.findById(dto.getEquipamentoId())
                    .orElseThrow(() -> new IllegalArgumentException("Equipamento não encontrado"));
                exercicio.setEquipamento(equip);
            } else {
                exercicio.setEquipamento(null);
            }
        }

        return exercicioRepository.save(exercicio);
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
        if (regiao == null || regiao.trim().isEmpty()) {
            throw new IllegalArgumentException("Região não pode estar vazia");
        }
        RegiaoCorpo regiaoEnum = RegiaoCorpo.fromString(regiao.trim());
        return exercicioRepository.countByRegiao(regiaoEnum);
    }

    @Transactional(readOnly = true)
    public boolean existeExercicio(Long id) {
        return id != null && id > 0 && exercicioRepository.existsById(id);
    }

    public String[] getRegioesValidas() {
        return RegiaoCorpo.getDescricoes();
    }

    public String[] getTiposValidos() {
        return TipoExercicio.getDescricoes();
    }
}
