package com.gymbro.service;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
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
        validarNomeUnico(dto.getNome());
        
        return Optional.of(dto)
                .map(EquipamentoDTO::toEntity)
                .map(repository::save)
                .map(EquipamentoDTO::fromEntity)
                .orElseThrow(() -> new IllegalStateException("Erro ao criar equipamento"));
    }

    @Transactional(readOnly = true)
    public EquipamentoDTO buscarPorId(Long id) {
        return repository.findById(id)
                .map(EquipamentoDTO::fromEntity)
                .orElseThrow(() -> new IllegalArgumentException("Equipamento não encontrado"));
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
        return Optional.ofNullable(nome)
                .filter(n -> !n.trim().isEmpty())
                .map(String::trim)
                .map(repository::findByNomeContainingIgnoreCaseOrderByNome)
                .orElseThrow(() -> new IllegalArgumentException("Nome para busca não pode estar vazio"))
                .stream()
                .map(EquipamentoDTO::fromEntity)
                .collect(Collectors.toList());
    }

    public EquipamentoDTO atualizarEquipamento(Long id, EquipamentoDTO dto) {
        return repository.findById(id)
                .map(equipamento -> atualizarDadosEquipamento(equipamento, dto))
                .map(repository::save)
                .map(EquipamentoDTO::fromEntity)
                .orElseThrow(() -> new IllegalArgumentException("Equipamento não encontrado"));
    }

    public void deletarEquipamento(Long id) {
        if (!repository.existsById(id)) {
            throw new java.util.NoSuchElementException("Equipamento não encontrado");
        }
        repository.deleteById(id);
    }

    @Transactional(readOnly = true)
    public List<EquipamentoDTO> buscarPorFaixaPeso(Float pesoMin, Float pesoMax) {
        validarFaixaPeso(pesoMin, pesoMax);
        
        return repository.findByPesoEquipBetweenOrderByPesoEquip(pesoMin, pesoMax)
                .stream()
                .map(EquipamentoDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<EquipamentoDTO> buscarPorCriterio(Predicate<Equipamento> criterio) {
        return repository.findAll()
                .stream()
                .filter(criterio)
                .map(EquipamentoDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public long contarEquipamentos() {
        return repository.count();
    }

    @Transactional(readOnly = true)
    public boolean existeEquipamento(Long id) {
        return Optional.ofNullable(id)
                .map(repository::existsById)
                .orElse(false);
    }

    private void validarNomeUnico(String nome) {
        if (repository.existsByNomeIgnoreCase(nome)) {
            throw new IllegalArgumentException("Já existe um equipamento com este nome");
        }
    }

    private void validarFaixaPeso(Float pesoMin, Float pesoMax) {
        if (pesoMin == null || pesoMax == null) {
            throw new IllegalArgumentException("Peso mínimo e máximo são obrigatórios");
        }
        if (pesoMin < 0 || pesoMax < 0) {
            throw new IllegalArgumentException("Pesos não podem ser negativos");
        }
        if (pesoMin > pesoMax) {
            throw new IllegalArgumentException("Peso mínimo não pode ser maior que o peso máximo");
        }
    }

    private Equipamento atualizarDadosEquipamento(Equipamento equipamento, EquipamentoDTO dto) {
        Optional.ofNullable(dto.getNome())
                .filter(nome -> !nome.trim().isEmpty())
                .map(String::trim)
                .filter(nome -> !nome.equalsIgnoreCase(equipamento.getNome()))
                .ifPresent(nome -> {
                    if (repository.existsByNomeIgnoreCaseAndIdNot(nome, equipamento.getId())) {
                        throw new IllegalArgumentException("Já existe outro equipamento com este nome");
                    }
                    equipamento.setNome(nome);
                });

        Optional.ofNullable(dto.getPesoEquip())
                .filter(peso -> peso >= 0)
                .ifPresentOrElse(
                    equipamento::setPesoEquip,
                    () -> {
                        if (dto.getPesoEquip() != null && dto.getPesoEquip() < 0) {
                            throw new IllegalArgumentException("Peso do equipamento não pode ser negativo");
                        }
                    }
                );

        return equipamento;
    }
}
