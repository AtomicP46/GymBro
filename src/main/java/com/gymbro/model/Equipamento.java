package com.gymbro.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "equipamentos")
public class Equipamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Nome do equipamento é obrigatório")
    @Column(nullable = false, length = 100)
    private String nome;

    @NotNull(message = "Peso do equipamento é obrigatório")
    @Min(value = 0, message = "Peso deve ser maior ou igual a zero")
    @Column(name = "peso_equip", nullable = false)
    private Float pesoEquip;

    public Equipamento() {
    }

    public Equipamento(String nome, Float pesoEquip) {
        this.nome = nome;
        this.pesoEquip = pesoEquip;
    }

    public Equipamento(Long id, String nome, Float pesoEquip) {
        this.id = id;
        this.nome = nome;
        this.pesoEquip = pesoEquip;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Float getPesoEquip() {
        return pesoEquip;
    }

    public void setPesoEquip(Float pesoEquip) {
        this.pesoEquip = pesoEquip;
    }

    @Override
    public String toString() {
        return "Equipamento{" +
            "id=" + id +
            ", nome='" + nome + '\'' +
            ", pesoEquip=" + pesoEquip +
            '}';
    }
}