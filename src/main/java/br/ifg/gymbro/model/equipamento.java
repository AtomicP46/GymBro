package br.ifg.gymbro.model;

public class Equipamento {
    private Long id;
    private String nome;
    private Float pesoEquip;

    // Construtores
    public Equipamento() {}

    public Equipamento(String nome, Float pesoEquip) {
        this.nome = nome;
        this.pesoEquip = pesoEquip;
    }

    public Equipamento(Long id, String nome, Float pesoEquip) {
        this.id = id;
        this.nome = nome;
        this.pesoEquip = pesoEquip;
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

    public Float getPesoEquip() {
        return pesoEquip;
    }

    public void setPesoEquip(Float pesoEquip) {
        this.pesoEquip = pesoEquip;
    }

    @Override
    public String toString() {
        return "Equipamento{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", pesoEquip=" + pesoEquip +
                '}';
    }
}
