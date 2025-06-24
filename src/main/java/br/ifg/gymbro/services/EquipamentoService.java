package br.ifg.gymbro.services;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import br.ifg.gymbro.dto.EquipamentoDTO;
import br.ifg.gymbro.model.Equipamento;
import br.ifg.gymbro.repository.EquipamentoRepository;

public class EquipamentoService {
    private EquipamentoRepository equipamentoRepository;

    public EquipamentoService(EquipamentoRepository equipamentoRepository) {
        this.equipamentoRepository = equipamentoRepository;
    }

    public Equipamento criarEquipamento(EquipamentoDTO equipamentoDTO) throws SQLException, IllegalArgumentException {
        // Validações
        if (equipamentoDTO.getNome() == null || equipamentoDTO.getNome().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome do equipamento é obrigatório");
        }

        if (equipamentoDTO.getPesoEquip() != null && equipamentoDTO.getPesoEquip() < 0) {
            throw new IllegalArgumentException("Peso do equipamento não pode ser negativo");
        }

        // Verificar se nome já existe
        if (equipamentoRepository.nomeExiste(equipamentoDTO.getNome())) {
            throw new IllegalArgumentException("Já existe um equipamento com este nome");
        }

        // Criar equipamento
        Equipamento equipamento = new Equipamento(
            equipamentoDTO.getNome().trim(),
            equipamentoDTO.getPesoEquip()
        );

        return equipamentoRepository.salvar(equipamento);
    }

    public Optional<Equipamento> buscarEquipamentoPorId(Long id) throws SQLException {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID deve ser um número positivo");
        }
        return equipamentoRepository.buscarPorId(id);
    }

    public List<Equipamento> listarTodosEquipamentos() throws SQLException {
        return equipamentoRepository.buscarTodos();
    }

    public List<Equipamento> buscarEquipamentosPorNome(String nome) throws SQLException {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome para busca não pode estar vazio");
        }
        return equipamentoRepository.buscarPorNome(nome.trim());
    }

    public Equipamento atualizarEquipamento(Long id, EquipamentoDTO equipamentoDTO) throws SQLException, IllegalArgumentException {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID deve ser um número positivo");
        }

        Optional<Equipamento> equipamentoOpt = equipamentoRepository.buscarPorId(id);
        
        if (equipamentoOpt.isEmpty()) {
            throw new IllegalArgumentException("Equipamento não encontrado");
        }

        Equipamento equipamento = equipamentoOpt.get();
        
        // Validar e atualizar nome
        if (equipamentoDTO.getNome() != null && !equipamentoDTO.getNome().trim().isEmpty()) {
            String novoNome = equipamentoDTO.getNome().trim();
            // Verificar se o novo nome já existe (exceto para o próprio equipamento)
            if (equipamentoRepository.nomeExisteExcetoId(novoNome, id)) {
                throw new IllegalArgumentException("Já existe outro equipamento com este nome");
            }
            equipamento.setNome(novoNome);
        }
        
        // Validar e atualizar peso
        if (equipamentoDTO.getPesoEquip() != null) {
            if (equipamentoDTO.getPesoEquip() < 0) {
                throw new IllegalArgumentException("Peso do equipamento não pode ser negativo");
            }
            equipamento.setPesoEquip(equipamentoDTO.getPesoEquip());
        }

        equipamentoRepository.atualizar(equipamento);
        return equipamento;
    }

    public void deletarEquipamento(Long id) throws SQLException, IllegalArgumentException {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID deve ser um número positivo");
        }

        Optional<Equipamento> equipamentoOpt = equipamentoRepository.buscarPorId(id);
        
        if (equipamentoOpt.isEmpty()) {
            throw new IllegalArgumentException("Equipamento não encontrado");
        }

        equipamentoRepository.deletar(id);
    }

    public List<Equipamento> buscarEquipamentosPorFaixaPeso(Float pesoMin, Float pesoMax) throws SQLException, IllegalArgumentException {
        if (pesoMin == null || pesoMax == null) {
            throw new IllegalArgumentException("Peso mínimo e máximo são obrigatórios");
        }

        if (pesoMin < 0 || pesoMax < 0) {
            throw new IllegalArgumentException("Pesos não podem ser negativos");
        }

        if (pesoMin > pesoMax) {
            throw new IllegalArgumentException("Peso mínimo não pode ser maior que o peso máximo");
        }

        return equipamentoRepository.buscarPorFaixaPeso(pesoMin, pesoMax);
    }

    public long contarEquipamentos() throws SQLException {
        return equipamentoRepository.contarEquipamentos();
    }

    public boolean equipamentoExiste(Long id) throws SQLException {
        if (id == null || id <= 0) {
            return false;
        }
        return equipamentoRepository.buscarPorId(id).isPresent();
    }
}
