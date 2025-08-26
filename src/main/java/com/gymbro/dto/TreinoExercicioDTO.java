package com.gymbro.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public class TreinoExercicioDTO {
    @NotNull(message = "ID do treino é obrigatório")
    private Long treinoId;
    
    @NotNull(message = "ID do exercício é obrigatório")
    private Long exercicioId;
    
    @Positive(message = "Número de séries deve ser positivo")
    private Integer series;
    
    @Positive(message = "Número de séries sugeridas deve ser positivo")
    private Integer seriesSugeridas;
    
    @Positive(message = "Número de repetições deve ser positivo")
    private Integer repeticoes;
    
    @Positive(message = "Número de repetições sugeridas deve ser positivo")
    private Integer repeticoesSugeridas;

    
    @PositiveOrZero(message = "Peso usado não pode ser negativo")
    private Float pesoUsado;
    
    private String anotacoes;
    private Boolean aquecimento;

    // Construtores
    public TreinoExercicioDTO() {}

    public TreinoExercicioDTO(Long treinoId, Long exercicioId, Integer series, Integer repeticoes, 
                             Float pesoUsado, String anotacoes, Boolean aquecimento, Integer seriesSugeridas, Integer repeticoesSugeridas) {
        this.treinoId = treinoId;
        this.exercicioId = exercicioId;
        this.series = series;
        this.repeticoes = repeticoes;
        this.pesoUsado = pesoUsado;
        this.anotacoes = anotacoes;
        this.aquecimento = aquecimento;
        this.seriesSugeridas = seriesSugeridas;
        this.repeticoesSugeridas = repeticoesSugeridas;
    }

    // Getters e Setters
    public Long getTreinoId() {
        return treinoId;
    }

    public void setTreinoId(Long treinoId) {
        this.treinoId = treinoId;
    }

    public Long getExercicioId() {
        return exercicioId;
    }

    public void setExercicioId(Long exercicioId) {
        this.exercicioId = exercicioId;
    }

    public Integer getSeries() {
        return series;
    }

    public void setSeries(Integer series) {
        this.series = series;
    }

    public Integer getRepeticoes() {
        return repeticoes;
    }

    public void setRepeticoes(Integer repeticoes) {
        this.repeticoes = repeticoes;
    }

    public Float getPesoUsado() {
        return pesoUsado;
    }

    public void setPesoUsado(Float pesoUsado) {
        this.pesoUsado = pesoUsado;
    }

    public String getAnotacoes() {
        return anotacoes;
    }

    public void setAnotacoes(String anotacoes) {
        this.anotacoes = anotacoes;
    }

    public Boolean getAquecimento() {
        return aquecimento;
    }

    public void setAquecimento(Boolean aquecimento) {
        this.aquecimento = aquecimento;
    }

    public Integer getSeriesSugeridas() {
        return seriesSugeridas;
    }

    public void setSeriesSugeridas(Integer seriesSugeridas) {
        this.seriesSugeridas = seriesSugeridas;
    }

    public Integer getRepeticoesSugeridas() {
        return repeticoesSugeridas;
    }

    public void setRepeticoesSugeridas(Integer repeticoesSugeridas) {
        this.repeticoesSugeridas = repeticoesSugeridas;
    }
}
