package com.gymbro.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

public class ExercicioProgressoDTO {
    @NotNull(message = "ID do treino exercício é obrigatório")
    private Long treinoExercicioId;
    
    @NotNull(message = "ID do treino é obrigatório")
    private Long treinoId;
    
    @NotNull(message = "ID do exercício é obrigatório")
    private Long exercicioId;
    
    private String exercicioNome;
    
    @Positive(message = "Número de séries deve ser positivo")
    private Integer series;
    
    @Positive(message = "Número de repetições deve ser positivo")
    private Integer repeticoes;
    
    @PositiveOrZero(message = "Peso usado não pode ser negativo")
    private Float pesoUsado;
    
    private String anotacoes;
    private Boolean aquecimento;
    private LocalDateTime dataHoraInicioTreino;
    private LocalDateTime dataHoraFimTreino;

    // Construtores
    public ExercicioProgressoDTO() {}

    public ExercicioProgressoDTO(Long treinoExercicioId, Long treinoId, Long exercicioId, String exercicioNome,
                                 Integer series, Integer repeticoes, Float pesoUsado, String anotacoes,
                                 Boolean aquecimento, LocalDateTime dataHoraInicioTreino, LocalDateTime dataHoraFimTreino) {
        this.treinoExercicioId = treinoExercicioId;
        this.treinoId = treinoId;
        this.exercicioId = exercicioId;
        this.exercicioNome = exercicioNome;
        this.series = series;
        this.repeticoes = repeticoes;
        this.pesoUsado = pesoUsado;
        this.anotacoes = anotacoes;
        this.aquecimento = aquecimento;
        this.dataHoraInicioTreino = dataHoraInicioTreino;
        this.dataHoraFimTreino = dataHoraFimTreino;
    }

    // Getters
    public Long getTreinoExercicioId() { return treinoExercicioId; }
    public Long getTreinoId() { return treinoId; }
    public Long getExercicioId() { return exercicioId; }
    public String getExercicioNome() { return exercicioNome; }
    public Integer getSeries() { return series; }
    public Integer getRepeticoes() { return repeticoes; }
    public Float getPesoUsado() { return pesoUsado; }
    public String getAnotacoes() { return anotacoes; }
    public Boolean getAquecimento() { return aquecimento; }
    public LocalDateTime getDataHoraInicioTreino() { return dataHoraInicioTreino; }
    public LocalDateTime getDataHoraFimTreino() { return dataHoraFimTreino; }

    // Setters (if needed, but for DTOs often not)
    public void setTreinoExercicioId(Long treinoExercicioId) { this.treinoExercicioId = treinoExercicioId; }
    public void setTreinoId(Long treinoId) { this.treinoId = treinoId; }
    public void setExercicioId(Long exercicioId) { this.exercicioId = exercicioId; }
    public void setExercicioNome(String exercicioNome) { this.exercicioNome = exercicioNome; }
    public void setSeries(Integer series) { this.series = series; }
    public void setRepeticoes(Integer repeticoes) { this.repeticoes = repeticoes; }
    public void setPesoUsado(Float pesoUsado) { this.pesoUsado = pesoUsado; }
    public void setAnotacoes(String anotacoes) { this.anotacoes = anotacoes; }
    public void setAquecimento(Boolean aquecimento) { this.aquecimento = aquecimento; }
    public void setDataHoraInicioTreino(LocalDateTime dataHoraInicioTreino) { this.dataHoraInicioTreino = dataHoraInicioTreino; }
    public void setDataHoraFimTreino(LocalDateTime dataHoraFimTreino) { this.dataHoraFimTreino = dataHoraFimTreino; }
}
