package br.ifg.gymbro.services;

import br.ifg.gymbro.dto.ExerciciosDTO;
import br.ifg.gymbro.enums.RegiaoCorpo;
import br.ifg.gymbro.enums.TipoExercicio;
import br.ifg.gymbro.model.Exercicios;
import br.ifg.gymbro.repository.ExerciciosRepository;
import br.ifg.gymbro.repository.EquipamentoRepository;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class ExerciciosService {
    private ExerciciosRepository exerciciosRepository;
    private EquipamentoRepository equipamentoRepository;

    public ExerciciosService(ExerciciosRepository exerciciosRepository, EquipamentoRepository equipamentoRepository) {
        this.exerciciosRepository = exerciciosRepository;
        this.equipamentoRepository = equipamentoRepository;
    }

    public Exercicios criarExercicio(ExerciciosDTO exercicioDTO) throws SQLException, IllegalArgumentException {
        // Validações
        if (exercicioDTO.getNome() == null || exercicioDTO.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do exercício é obrigatório");
        }

        if (exercicioDTO.getRegiao() == null || exercicioDTO.getRegiao().trim().isEmpty()) {
            throw new IllegalArgumentException("Região do exercício é obrigatória");
        }

        if (exercicioDTO.getTipo() == null || exercicioDTO.getTipo().trim().isEmpty()) {
            throw new IllegalArgumentException("Tipo do exercício é obrigatório");
        }

        if (exercicioDTO.getUnilateral() == null) {
            throw new IllegalArgumentException("Informação sobre exercício unilateral é obrigatória");
        }

        // Validar região
        if (!RegiaoCorpo.isValida(exercicioDTO.getRegiao())) {
            throw new IllegalArgumentException("Região inválida. Regiões válidas: " + String.join(", ", RegiaoCorpo.getDescricoes()));
        }

        // Validar tipo
        if (!TipoExercicio.isValido(exercicioDTO.getTipo())) {
            throw new IllegalArgumentException("Tipo inválido. Tipos válidos: " + String.join(", ", TipoExercicio.getDescricoes()));
        }

        // Verificar se nome já existe
        if (exerciciosRepository.nomeExiste(exercicioDTO.getNome())) {
            throw new IllegalArgumentException("Já existe um exercício com este nome");
        }

        // Verificar se equipamento existe (se informado)
        if (exercicioDTO.getEquipamentoId() != null) {
            Optional<br.ifg.gymbro.model.Equipamento> equipamento = equipamentoRepository.buscarPorId(exercicioDTO.getEquipamentoId());
            if (equipamento.isEmpty()) {
                throw new IllegalArgumentException("Equipamento não encontrado");
            }
        }

        // Criar exercício
        Exercicios exercicio = new Exercicios(
            exercicioDTO.getNome().trim(),
            exercicioDTO.getRegiao().trim(),
            exercicioDTO.getTipo().trim(),
            exercicioDTO.getUnilateral(),
            exercicioDTO.getEquipamentoId()
        );

        return exerciciosRepository.salvar(exercicio);
    }

    public Optional<Exercicios> buscarExercicioPorId(Long id) throws SQLException {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID deve ser um número positivo");
        }
        return exerciciosRepository.buscarPorId(id);
    }

    public List<Exercicios> listarTodosExercicios() throws SQLException {
        return exerciciosRepository.buscarTodos();
    }

    public List<Exercicios> buscarExerciciosPorNome(String nome) throws SQLException {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome para busca não pode estar vazio");
        }
        return exerciciosRepository.buscarPorNome(nome.trim());
    }

    public List<Exercicios> buscarExerciciosPorRegiao(String regiao) throws SQLException {
        if (regiao == null || regiao.trim().isEmpty()) {
            throw new IllegalArgumentException("Região para busca não pode estar vazia");
        }

        if (!RegiaoCorpo.isValida(regiao)) {
            throw new IllegalArgumentException("Região inválida. Regiões válidas: " + String.join(", ", RegiaoCorpo.getDescricoes()));
        }

        return exerciciosRepository.buscarPorRegiao(regiao.trim());
    }

    public List<Exercicios> buscarExerciciosPorTipo(String tipo) throws SQLException {
        if (tipo == null || tipo.trim().isEmpty()) {
            throw new IllegalArgumentException("Tipo para busca não pode estar vazio");
        }

        if (!TipoExercicio.isValido(tipo)) {
            throw new IllegalArgumentException("Tipo inválido. Tipos válidos: " + String.join(", ", TipoExercicio.getDescricoes()));
        }

        return exerciciosRepository.buscarPorTipo(tipo.trim());
    }

    public List<Exercicios> buscarExerciciosPorEquipamento(Long equipamentoId) throws SQLException {
        if (equipamentoId == null || equipamentoId <= 0) {
            throw new IllegalArgumentException("ID do equipamento deve ser um número positivo");
        }

        // Verificar se equipamento existe
        Optional<br.ifg.gymbro.model.Equipamento> equipamento = equipamentoRepository.buscarPorId(equipamentoId);
        if (equipamento.isEmpty()) {
            throw new IllegalArgumentException("Equipamento não encontrado");
        }

        return exerciciosRepository.buscarPorEquipamento(equipamentoId);
    }

    public List<Exercicios> buscarExerciciosPorUnilateral(boolean unilateral) throws SQLException {
        return exerciciosRepository.buscarPorUnilateral(unilateral);
    }

    public Exercicios atualizarExercicio(Long id, ExerciciosDTO exercicioDTO) throws SQLException, IllegalArgumentException {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID deve ser um número positivo");
        }

        Optional<Exercicios> exercicioOpt = exerciciosRepository.buscarPorId(id);
        
        if (exercicioOpt.isEmpty()) {
            throw new IllegalArgumentException("Exercício não encontrado");
        }

        Exercicios exercicio = exercicioOpt.get();
        
        // Validar e atualizar nome
        if (exercicioDTO.getNome() != null && !exercicioDTO.getNome().trim().isEmpty()) {
            String novoNome = exercicioDTO.getNome().trim();
            // Verificar se o novo nome já existe (exceto para o próprio exercício)
            if (exerciciosRepository.nomeExisteExcetoId(novoNome, id)) {
                throw new IllegalArgumentException("Já existe outro exercício com este nome");
            }
            exercicio.setNome(novoNome);
        }
        
        // Validar e atualizar região
        if (exercicioDTO.getRegiao() != null && !exercicioDTO.getRegiao().trim().isEmpty()) {
            String novaRegiao = exercicioDTO.getRegiao().trim();
            if (!RegiaoCorpo.isValida(novaRegiao)) {
                throw new IllegalArgumentException("Região inválida. Regiões válidas: " + String.join(", ", RegiaoCorpo.getDescricoes()));
            }
            exercicio.setRegiao(novaRegiao);
        }

        // Validar e atualizar tipo
        if (exercicioDTO.getTipo() != null && !exercicioDTO.getTipo().trim().isEmpty()) {
            String novoTipo = exercicioDTO.getTipo().trim();
            if (!TipoExercicio.isValido(novoTipo)) {
                throw new IllegalArgumentException("Tipo inválido. Tipos válidos: " + String.join(", ", TipoExercicio.getDescricoes()));
            }
            exercicio.setTipo(novoTipo);
        }

        // Atualizar unilateral
        if (exercicioDTO.getUnilateral() != null) {
            exercicio.setUnilateral(exercicioDTO.getUnilateral());
        }

        // Validar e atualizar equipamento
        if (exercicioDTO.getEquipamentoId() != null) {
            if (exercicioDTO.getEquipamentoId() > 0) {
                Optional<br.ifg.gymbro.model.Equipamento> equipamento = equipamentoRepository.buscarPorId(exercicioDTO.getEquipamentoId());
                if (equipamento.isEmpty()) {
                    throw new IllegalArgumentException("Equipamento não encontrado");
                }
                exercicio.setEquipamentoId(exercicioDTO.getEquipamentoId());
            } else {
                exercicio.setEquipamentoId(null); // Remove equipamento
            }
        }

        exerciciosRepository.atualizar(exercicio);
        return exerciciosRepository.buscarPorId(id).orElse(exercicio);
    }

    public void deletarExercicio(Long id) throws SQLException, IllegalArgumentException {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID deve ser um número positivo");
        }

        Optional<Exercicios> exercicioOpt = exerciciosRepository.buscarPorId(id);
        
        if (exercicioOpt.isEmpty()) {
            throw new IllegalArgumentException("Exercício não encontrado");
        }

        exerciciosRepository.deletar(id);
    }

    public long contarExercicios() throws SQLException {
        return exerciciosRepository.contarExercicios();
    }

    public long contarExerciciosPorRegiao(String regiao) throws SQLException {
        if (regiao == null || regiao.trim().isEmpty()) {
            throw new IllegalArgumentException("Região não pode estar vazia");
        }

        if (!RegiaoCorpo.isValida(regiao)) {
            throw new IllegalArgumentException("Região inválida");
        }

        return exerciciosRepository.contarPorRegiao(regiao.trim());
    }

    public boolean exercicioExiste(Long id) throws SQLException {
        if (id == null || id <= 0) {
            return false;
        }
        return exerciciosRepository.buscarPorId(id).isPresent();
    }

    public String[] getRegioesValidas() {
        return RegiaoCorpo.getDescricoes();
    }

    public String[] getTiposValidos() {
        return TipoExercicio.getDescricoes();
    }
}
