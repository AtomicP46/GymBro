package com.gymbro.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.gymbro.model.Treino;

@Repository
public interface TreinoRepository extends JpaRepository<Treino, Long> {
    
    @Query("SELECT t FROM Treino t WHERE t.usuarioId = :usuarioId ORDER BY t.dataHoraInicio DESC")
    List<Treino> findByUsuarioIdOrderByDataHoraInicioDesc(@Param("usuarioId") Long usuarioId);
    
    @Query("SELECT t FROM Treino t WHERE t.personalId = :personalId ORDER BY t.dataHoraInicio DESC")
    List<Treino> findByPersonalIdOrderByDataHoraInicioDesc(@Param("personalId") Long personalId);
    
    @Query("SELECT t FROM Treino t WHERE t.dataHoraInicio IS NOT NULL AND t.dataHoraFim IS NULL ORDER BY t.dataHoraInicio DESC")
    List<Treino> findTreinosEmAndamento();
    
    @Query("SELECT COUNT(t) FROM Treino t WHERE t.dataHoraFim IS NOT NULL")
    long countTreinosFinalizados();
    
    List<Treino> findAllByOrderByDataHoraInicioDesc();
}
