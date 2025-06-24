package br.ifg.gymbro.model;

public class TreinoExercicio {
    private Long id;
    private Long treinoId;
    private Long exercicioId;
    private Integer series;
    private Integer repeticoes;
    private Float pesoUsado;
    private String anotacoes;
    private Boolean aquecimento;
    
    // Para exibição
    private String exercicioNome;
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
