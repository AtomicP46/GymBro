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

@Entity
@Table(name = "plano_exercicio")
public class PlanoExercicio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull(message = "ID do plano é obrigatório")
    @Column(name = "plano_id", nullable = false)
    private Long planoId;
    
    @NotNull(message = "ID do exercício é obrigatório")
    @Column(name = "exercicio_id", nullable = false)
    private Long exercicioId;
    
    @Positive(message = "Número de séries deve ser positivo")
    @Column(name = "series_sugeridas")
    private Integer seriesSugeridas;
    
    @Column(length = 500)
    private String observacoes;
    
    @Column(nullable = false)
    private Boolean aquecimento = false;
    
    // Campos auxiliares para exibição (não persistidos)
    @Transient
    private String exercicioNome;
    
    @Transient
    private String exercicioRegiao;

    // Construtores
    public PlanoExercicio() {}

    public PlanoExercicio(Long planoId, Long exercicioId, Integer seriesSugeridas, String observacoes, Boolean aquecimento) {
        this.planoId = planoId;
        this.exercicioId = exercicioId;
        this.seriesSugeridas = seriesSugeridas;
        this.observacoes = observacoes;
        this.aquecimento = aquecimento;
    }

    public PlanoExercicio(Long id, Long planoId, Long exercicioId, Integer seriesSugeridas, String observacoes, Boolean aquecimento) {
        this.id = id;
        this.planoId = planoId;
        this.exercicioId = exercicioId;
        this.seriesSugeridas = seriesSugeridas;
        this.observacoes = observacoes;
        this.aquecimento = aquecimento;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public boolean isAquecimento() {
        return aquecimento != null ? aquecimento : false;
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

    @Override
    public String toString() {
        return "PlanoExercicio{" +
                "id=" + id +
                ", planoId=" + planoId +
                ", exercicioId=" + exercicioId +
                ", seriesSugeridas=" + seriesSugeridas +
                ", observacoes='" + observacoes + '\'' +
                ", aquecimento=" + aquecimento +
                ", exercicioNome='" + exercicioNome + '\'' +
                ", exercicioRegiao='" + exercicioRegiao + '\'' +
                '}';
    }
}
