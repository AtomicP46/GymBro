package br.ifg.gymbro.model;

public class Exercicios {
    private Long id;
    private String nome;
    private String regiao;
    private String tipo;
    private Boolean unilateral;
    private Long equipamentoId;
    
    // Para exibição, vamos incluir o nome do equipamento
    private String equipamentoNome;

    // Construtores
    public Exercicios() {}

    public Exercicios(String nome, String regiao, String tipo, Boolean unilateral, Long equipamentoId) {
        this.nome = nome;
        this.regiao = regiao;
        this.tipo = tipo;
        this.unilateral = unilateral;
        this.equipamentoId = equipamentoId;
    }

    public Exercicios(Long id, String nome, String regiao, String tipo, Boolean unilateral, Long equipamentoId) {
        this.id = id;
        this.nome = nome;
        this.regiao = regiao;
        this.tipo = tipo;
        this.unilateral = unilateral;
        this.equipamentoId = equipamentoId;
    }

    // Getters e Setters
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

    public String getEquipamentoNome() {
        return equipamentoNome;
    }

    public void setEquipamentoNome(String equipamentoNome) {
        this.equipamentoNome = equipamentoNome;
    }

    // Método para verificar se é unilateral
    public boolean isUnilateral() {
        return unilateral != null && unilateral;
    }

    @Override
    public String toString() {
        return "Exercicios{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", regiao='" + regiao + '\'' +
                ", tipo='" + tipo + '\'' +
                ", unilateral=" + unilateral +
                ", equipamentoId=" + equipamentoId +
                ", equipamentoNome='" + equipamentoNome + '\'' +
                '}';
    }
}
