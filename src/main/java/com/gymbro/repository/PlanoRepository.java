package com.gymbro.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.gymbro.enums.TipoCriador;
import com.gymbro.model.Plano;

@Repository
public interface PlanoRepository extends JpaRepository<Plano, Long> {
    
    List<Plano> findByCriadorIdAndTipoCriadorOrderByDataCriacaoDesc(Long criadorId, TipoCriador tipoCriador);
    
    List<Plano> findByPublicoTrueOrderByDataCriacaoDesc();
    
    @Query("SELECT p FROM Plano p WHERE LOWER(p.nome) LIKE LOWER(CONCAT('%', :nome, '%')) ORDER BY p.dataCriacao DESC")
    List<Plano> findByNomeContainingIgnoreCaseOrderByDataCriacaoDesc(@Param("nome") String nome);
    
    List<Plano> findAllByOrderByDataCriacaoDesc();
}
