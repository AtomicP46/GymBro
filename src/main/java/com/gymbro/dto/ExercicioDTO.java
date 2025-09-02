package com.gymbro.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ExercicioDTO {

    private Long id;

    @NotBlank(message = "Nome do exercício é obrigatório")
    private String nome;

    @NotBlank(message = "Região do exercício é obrigatória")
    private String regiao;

    @NotBlank(message = "Tipo de exercício é obrigatório")
    private String tipo;

    @NotNull(message = "Indicação de unilateralidade é obrigatória")
    private Boolean unilateral;

    // Campo agora opcional: removida a anotação @NotNull
    private Long equipamentoId;

    public ExercicioDTO() {
    }

    public ExercicioDTO(String nome,
                        String regiao,
                        String tipo,
                        Boolean unilateral,
                        Long equipamentoId) {
        this.nome = nome;
        this.regiao = regiao;
        this.tipo = tipo;
        this.unilateral = unilateral;
        this.equipamentoId = equipamentoId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getRegiao() {
        return regiao;
    }

    public void setRegiao(String regiao) {
        this.regiao = regiao;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Boolean getUnilateral() {
        return unilateral;
    }

    public void setUnilateral(Boolean unilateral) {
        this.unilateral = unilateral;
    }

    public Long getEquipamentoId() {
        return equipamentoId;
    }

    public void setEquipamentoId(Long equipamentoId) {
        this.equipamentoId = equipamentoId;
    }
}