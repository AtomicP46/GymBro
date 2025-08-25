package com.gymbro.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.gymbro.model.PlanoExercicio;

@Repository
public interface PlanoExercicioRepository extends JpaRepository<PlanoExercicio, Long> {
    
    @Query("SELECT pe FROM PlanoExercicio pe WHERE pe.planoId = :planoId ORDER BY pe.aquecimento DESC, pe.id")
    List<PlanoExercicio> findByPlanoIdOrderByAquecimentoDescId(@Param("planoId") Long planoId);
    
    List<PlanoExercicio> findByPlanoIdAndAquecimentoTrueOrderById(Long planoId);
    
    List<PlanoExercicio> findByPlanoIdAndAquecimentoFalseOrderById(Long planoId);
    
    boolean existsByPlanoIdAndExercicioId(Long planoId, Long exercicioId);
    
    void deleteByPlanoId(Long planoId);
    
    @Query("SELECT COUNT(pe) > 0 FROM PlanoExercicio pe WHERE pe.planoId = :planoId AND pe.exercicioId = :exercicioId")
    boolean existeExercicioNoPlano(@Param("planoId") Long planoId, @Param("exercicioId") Long exercicioId);
}
