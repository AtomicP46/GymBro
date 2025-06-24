package br.ifg.gymbro.dto;

public class EquipamentoDTO {
    private String nome;
    private Float pesoEquip;

    // Construtores
    public EquipamentoDTO() {}

    public EquipamentoDTO(String nome, Float pesoEquip) {
        this.nome = nome;
        this.pesoEquip = pesoEquip;
    }

    // Getters e Setters
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
}
