package com.gymbro.repository;

import com.gymbro.model.Exercicio;
import com.gymbro.enums.RegiaoCorpo;
import com.gymbro.enums.TipoExercicio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExercicioRepository extends JpaRepository<Exercicio, Long> {

    boolean existsByNomeIgnoreCase(String nome);

    boolean existsByNomeIgnoreCaseAndIdNot(String nome, Long id);

    List<Exercicio> findByNomeContainingIgnoreCaseOrderByNome(String nome);

    List<Exercicio> findByRegiaoOrderByNome(RegiaoCorpo regiao);

    List<Exercicio> findByTipoOrderByNome(TipoExercicio tipo);

    List<Exercicio> findByEquipamentoIdOrderByNome(Long equipamentoId);

    List<Exercicio> findByUnilateralOrderByNome(Boolean unilateral);

    long countByRegiao(RegiaoCorpo regiao);
}