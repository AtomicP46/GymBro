package com.gymbro.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

@Entity
@Table(name = "treino_exercicio")
public class TreinoExercicio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull(message = "ID do treino é obrigatório")
    @Column(name = "treino_id", nullable = false)
    private Long treinoId;
    
    @NotNull(message = "ID do exercício é obrigatório")
    @Column(name = "exercicio_id", nullable = false)
    private Long exercicioId;
    
    @Positive(message = "Número de séries deve ser positivo")
    private Integer series;
    
    @Positive(message = "Número de repetições deve ser positivo")
    private Integer repeticoes;
    
    @PositiveOrZero(message = "Peso usado não pode ser negativo")
    @Column(name = "peso_usado")
    private Float pesoUsado;
    
    private String anotacoes;
    
    @Column(columnDefinition = "boolean default false")
    private Boolean aquecimento = false;
    
    // Para exibição - não persistidos
    @Transient
    private String exercicioNome;
    
    @Transient
    private String exercicioRegiao;

    // Construtores
    public TreinoExercicio() {}

    public TreinoExercicio(Long treinoId, Long exercicioId, Integer series, Integer repeticoes, 
                          Float pesoUsado, String anotacoes, Boolean aquecimento) {
        this.treinoId = treinoId;
        this.exercicioId = exercicioId;
        this.series = series;
        this.repeticoes = repeticoes;
        this.pesoUsado = pesoUsado;
        this.anotacoes = anotacoes;
        this.aquecimento = aquecimento;
    }

    public TreinoExercicio(Long id, Long treinoId, Long exercicioId, Integer series, Integer repeticoes, 
                          Float pesoUsado, String anotacoes, Boolean aquecimento) {
        this.id = id;
        this.treinoId = treinoId;
        this.exercicioId = exercicioId;
        this.series = series;
        this.repeticoes = repeticoes;
        this.pesoUsado = pesoUsado;
        this.anotacoes = anotacoes;
        this.aquecimento = aquecimento;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public String getExercicioNome() {
        return exercicioNome;
    }

    public void setExercicioNome(String exercicioNome) {
        this.exercicioNome = exercicioNome;
    }

    public String getExercicioRegiao() {
        return exercicioRegiao;
    }

    public void setExercicioRegiao(String exercicioRegiao) {
        this.exercicioRegiao = exercicioRegiao;
    }

    public boolean isAquecimento() {
        return aquecimento != null && aquecimento;
    }

    @Override
    public String toString() {
        return "TreinoExercicio{" +
                "id=" + id +
                ", treinoId=" + treinoId +
                ", exercicioId=" + exercicioId +
                ", series=" + series +
                ", repeticoes=" + repeticoes +
                ", pesoUsado=" + pesoUsado +
                ", anotacoes='" + anotacoes + '\'' +
                ", aquecimento=" + aquecimento +
                '}';
    }
}
