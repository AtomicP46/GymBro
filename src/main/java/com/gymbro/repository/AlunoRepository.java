package com.gymbro.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gymbro.model.Aluno;

@Repository
public interface AlunoRepository extends JpaRepository<Aluno, Long> {

    Optional<Aluno> findByEmail(String email);

    boolean existsByEmail(String email);

    List<Aluno> findByPesoBetween(BigDecimal pesoMin, BigDecimal pesoMax);

    List<Aluno> findByPesoGreaterThanEqual(BigDecimal peso);

    List<Aluno> findByPesoLessThanEqual(BigDecimal peso);
}