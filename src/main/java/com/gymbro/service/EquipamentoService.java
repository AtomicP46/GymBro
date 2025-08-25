package com.gymbro.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gymbro.dto.EquipamentoDTO;
import com.gymbro.model.Equipamento;
import com.gymbro.repository.EquipamentoRepository;

@Service
@Transactional
public class EquipamentoService {

    private final EquipamentoRepository repository;

    @Autowired
    public EquipamentoService(EquipamentoRepository repository) {
        this.repository = repository;
    }

    public EquipamentoDTO criarEquipamento(EquipamentoDTO dto) {
        if (repository.existsByNomeIgnoreCase(dto.getNome())) {
            throw new IllegalArgumentException("Já existe um equipamento com este nome");
        }
        Equipamento equipamento = dto.toEntity();
        Equipamento salvo = repository.save(equipamento);
        return EquipamentoDTO.fromEntity(salvo);
    }

    @Transactional(readOnly = true)
    public EquipamentoDTO buscarPorId(Long id) {
        Equipamento equipamento = repository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Equipamento não encontrado"));
        return EquipamentoDTO.fromEntity(equipamento);
    }

    @Transactional(readOnly = true)
    public List<EquipamentoDTO> listarTodos() {
        return repository.findAll()
            .stream()
            .map(EquipamentoDTO::fromEntity)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<EquipamentoDTO> buscarPorNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new IllegalArgumentException("Nome para busca não pode estar vazio");
        }
        return repository
            .findByNomeContainingIgnoreCaseOrderByNome(nome.trim())
            .stream()
            .map(EquipamentoDTO::fromEntity)
            .collect(Collectors.toList());
    }

    public EquipamentoDTO atualizarEquipamento(Long id, EquipamentoDTO dto) {
        Equipamento equipamento = repository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("Equipamento não encontrado"));

        String novoNome = dto.getNome();
        if (novoNome != null && !novoNome.trim().isEmpty() 
            && !novoNome.equalsIgnoreCase(equipamento.getNome())) {
            String trimmed = novoNome.trim();
            if (repository.existsByNomeIgnoreCaseAndIdNot(trimmed, id)) {
                throw new IllegalArgumentException("Já existe outro equipamento com este nome");
            }
            equipamento.setNome(trimmed);
        }

        if (dto.getPesoEquip() != null) {
            if (dto.getPesoEquip() < 0) {
                throw new IllegalArgumentException("Peso do equipamento não pode ser negativo");
            }
            equipamento.setPesoEquip(dto.getPesoEquip());
        }

        Equipamento atualizado = repository.save(equipamento);
        return EquipamentoDTO.fromEntity(atualizado);
    }

    public void deletarEquipamento(Long id) {
        if (!repository.existsById(id)) {
            throw new java.util.NoSuchElementException("Equipamento não encontrado");
        }
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<EquipamentoDTO> buscarPorFaixaPeso(Float pesoMin, Float pesoMax) {
        if (pesoMin == null || pesoMax == null) {
            throw new IllegalArgumentException("Peso mínimo e máximo são obrigatórios");
        }
        if (pesoMin < 0 || pesoMax < 0) {
            throw new IllegalArgumentException("Pesos não podem ser negativos");
        }
        if (pesoMin > pesoMax) {
            throw new IllegalArgumentException("Peso mínimo não pode ser maior que o peso máximo");
        }
        return repository
            .findByPesoEquipBetweenOrderByPesoEquip(pesoMin, pesoMax)
            .stream()
            .map(EquipamentoDTO::fromEntity)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public long contarEquipamentos() {
        return repository.count();
    }

    @Transactional(readOnly = true)
    public boolean existeEquipamento(Long id) {
        return id != null && repository.existsById(id);
    }
}
