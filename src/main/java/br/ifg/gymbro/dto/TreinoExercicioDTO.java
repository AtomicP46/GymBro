package br.ifg.gymbro.dto;

public class TreinoExercicioDTO {
    private Long treinoId;
    private Long exercicioId;
    private Integer series;
    private Integer repeticoes;
    private Float pesoUsado;
    private String anotacoes;
    private Boolean aquecimento;

    // Construtores
    public TreinoExercicioDTO() {}

    public TreinoExercicioDTO(Long treinoId, Long exercicioId, Integer series, Integer repeticoes, 
                             Float pesoUsado, String anotacoes, Boolean aquecimento) {
        this.treinoId = treinoId;
        this.exercicioId = exercicioId;
        this.series = series;
        this.repeticoes = repeticoes;
        this.pesoUsado = pesoUsado;
        this.anotacoes = anotacoes;
        this.aquecimento = aquecimento;
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
}
