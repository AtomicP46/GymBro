package com.gymbro.repository;

import com.gymbro.dto.ExercicioProgressoDTO;
import com.gymbro.model.TreinoExercicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TreinoExercicioRepository extends JpaRepository<TreinoExercicio, Long> {
    
    List<TreinoExercicio> findByTreinoIdOrderById(Long treinoId);
    
    boolean existsByTreinoIdAndExercicioId(Long treinoId, Long exercicioId);
    
    void deleteByTreinoId(Long treinoId);
    
    @Query("""
        SELECT new com.gymbro.dto.ExercicioProgressoDTO(
            te.id, te.treinoId, te.exercicioId, e.nome,
            te.series, te.repeticoes, te.pesoUsado, te.anotacoes, te.aquecimento,
            t.dataHoraInicio, t.dataHoraFim
        )
        FROM TreinoExercicio te 
        JOIN Treino t ON te.treinoId = t.id 
        JOIN Exercicio e ON te.exercicioId = e.id 
        WHERE t.usuarioId = :usuarioId AND te.exercicioId = :exercicioId 
        ORDER BY t.dataHoraInicio DESC
    """)
    List<ExercicioProgressoDTO> findHistoricoExercicioPorUsuario(
        @Param("usuarioId") Long usuarioId, 
        @Param("exercicioId") Long exercicioId
    );
}
