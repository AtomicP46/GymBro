package com.gymbro.model;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;

@Entity
@Table(name = "alunos")
public class Aluno extends Usuario {

    @DecimalMin(value = "0.1", message = "Peso deve ser maior que 0")
    @Column(name = "peso", precision = 5, scale = 2, nullable = false)
    private BigDecimal peso;

    public Aluno() {
        super();
    }

    public Aluno(String nome,
                 String email,
                 String senhaHash,
                 LocalDate dataNascimento,
                 BigDecimal peso) {
        super(nome, email, senhaHash, dataNascimento);
        this.peso = peso;
    }

    public BigDecimal getPeso() {
        return peso;
    }

    public void setPeso(BigDecimal peso) {
        this.peso = peso;
    }

    @Override
    public String toString() {
        return "Aluno{" +
               "id=" + getId() +
               ", nome='" + getNome() + '\'' +
               ", idade=" + getIdade() +
               ", peso=" + peso +
               '}';
    }
}