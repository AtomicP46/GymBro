package com.gymbro.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gymbro.model.Equipamento;

@Repository
public interface EquipamentoRepository extends JpaRepository<Equipamento, Long> {

    /**
     * Verifica se já existe um equipamento com o mesmo nome (case-insensitive).
     */
    boolean existsByNomeIgnoreCase(String nome);

    /**
     * Verifica se já existe um equipamento com o mesmo nome (case-insensitive), 
     * excluindo o registro de um id específico.
     */
    boolean existsByNomeIgnoreCaseAndIdNot(String nome, Long id);

    /**
     * Busca equipamentos cujo nome contenha o termo informado (case-insensitive),
     * ordenados pelo nome.
     */
    List<Equipamento> findByNomeContainingIgnoreCaseOrderByNome(String nome);

    /**
     * Busca equipamentos cujo peso esteja entre os valores informados,
     * ordenados pelo peso.
     */
    List<Equipamento> findByPesoEquipBetweenOrderByPesoEquip(Float pesoMin, Float pesoMax);
}