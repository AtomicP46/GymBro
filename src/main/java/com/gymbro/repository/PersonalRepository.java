package com.gymbro.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.gymbro.model.Personal;

@Repository
public interface PersonalRepository extends JpaRepository<Personal, Long> {

    Optional<Personal> findByEmail(String email);

    boolean existsByEmail(String email);

    List<Personal> findByFormado(Boolean formado);

    List<Personal> findByCodigoValidacaoNotNull();

    List<Personal> findByLicencaNotNull();
}