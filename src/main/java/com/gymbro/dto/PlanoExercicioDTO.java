package com.gymbro.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class PlanoExercicioDTO {
    @NotNull(message = "ID do plano é obrigatório")
    private Long planoId;
    
    @NotNull(message = "ID do exercício é obrigatório")
    private Long exercicioId;
    
    @Positive(message = "Número de séries deve ser positivo")
    private Integer seriesSugeridas;
    
    private String observacoes;
    
    private Boolean aquecimento = false;

    // Construtores
    public PlanoExercicioDTO() {}

    public PlanoExercicioDTO(Long planoId, Long exercicioId, Integer seriesSugeridas, String observacoes, Boolean aquecimento) {
        this.planoId = planoId;
        this.exercicioId = exercicioId;
        this.seriesSugeridas = seriesSugeridas;
        this.observacoes = observacoes;
        this.aquecimento = aquecimento;
    }

    // Getters e Setters
    public Long getPlanoId() {
        return planoId;
    }

    public void setPlanoId(Long planoId) {
        this.planoId = planoId;
    }

    public Long getExercicioId() {
        return exercicioId;
    }

    public void setExercicioId(Long exercicioId) {
        this.exercicioId = exercicioId;
    }

    public Integer getSeriesSugeridas() {
        return seriesSugeridas;
    }

    public void setSeriesSugeridas(Integer seriesSugeridas) {
        this.seriesSugeridas = seriesSugeridas;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public Boolean getAquecimento() {
        return aquecimento;
    }

    public void setAquecimento(Boolean aquecimento) {
        this.aquecimento = aquecimento;
    }
}
