package br.ifg.gymbro.dto;

import br.ifg.gymbro.enums.TipoCriador;

public class PlanoDTO {
    private String nome;
    private String descricao;
    private Long criadorId;
    private TipoCriador tipoCriador;
    private Boolean publico;
    private String observacoes;

    // Construtores
    public PlanoDTO() {}

    public PlanoDTO(String nome, String descricao, Long criadorId, TipoCriador tipoCriador, Boolean publico, String observacoes) {
        this.nome = nome;
        this.descricao = descricao;
        this.criadorId = criadorId;
        this.tipoCriador = tipoCriador;
        this.publico = publico;
        this.observacoes = observacoes;
    }

    // Getters e Setters
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Long getCriadorId() {
        return criadorId;
    }

    public void setCriadorId(Long criadorId) {
        this.criadorId = criadorId;
    }

    public TipoCriador getTipoCriador() {
        return tipoCriador;
    }

    public void setTipoCriador(TipoCriador tipoCriador) {
        this.tipoCriador = tipoCriador;
    }

    public Boolean getPublico() {
        return publico;
    }

    public void setPublico(Boolean publico) {
        this.publico = publico;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }
}
