package br.ifg.gymbro.dto;

public class ExerciciosDTO {
    private String nome;
    private String regiao;
    private String tipo;
    private Boolean unilateral;
    private Long equipamentoId;

    // Construtores
    public ExerciciosDTO() {}

    public ExerciciosDTO(String nome, String regiao, String tipo, Boolean unilateral, Long equipamentoId) {
        this.nome = nome;
        this.regiao = regiao;
        this.tipo = tipo;
        this.unilateral = unilateral;
        this.equipamentoId = equipamentoId;
    }

    // Getters e Setters
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
