package br.ifg.gymbro.model;

import java.time.LocalDateTime;

import br.ifg.gymbro.enums.TipoCriador;

public class Plano {
    private Long id;
    private String nome;
    private String descricao;
    private Long criadorId;
    private TipoCriador tipoCriador;
    private Boolean publico;
    private String observacoes;
    private LocalDateTime dataCriacao;

    // Construtores
    public Plano() {}

    public Plano(String nome, String descricao, Long criadorId, TipoCriador tipoCriador, Boolean publico, String observacoes) {
        this.nome = nome;
        this.descricao = descricao;
        this.criadorId = criadorId;
        this.tipoCriador = tipoCriador;
        this.publico = publico;
        this.observacoes = observacoes;
    }

    public Plano(Long id, String nome, String descricao, Long criadorId, TipoCriador tipoCriador, Boolean publico, String observacoes, LocalDateTime dataCriacao) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
        this.criadorId = criadorId;
        this.tipoCriador = tipoCriador;
        this.publico = publico;
        this.observacoes = observacoes;
        this.dataCriacao = dataCriacao;
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

    public boolean isPublico() {
        return publico != null ? publico : false;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public LocalDateTime getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDateTime dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    @Override
    public String toString() {
        return "Plano{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", descricao='" + descricao + '\'' +
                ", criadorId=" + criadorId +
                ", tipoCriador=" + tipoCriador +
                ", publico=" + publico +
                ", observacoes='" + observacoes + '\'' +
                ", dataCriacao=" + dataCriacao +
                '}';
    }
}
