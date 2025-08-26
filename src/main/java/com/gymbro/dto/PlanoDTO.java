package com.gymbro.dto;

import com.gymbro.enums.TipoCriador;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class PlanoDTO {
    private Long id;
    
    @NotBlank(message = "Nome do plano é obrigatório")
    private String nome;
    
    @NotBlank(message = "Descrição do plano é obrigatória")
    private String descricao;
    
    @NotNull(message = "ID do criador é obrigatório")
    private Long criadorId;
    
    @NotNull(message = "Tipo do criador é obrigatório")
    private TipoCriador tipoCriador;
    
    private Boolean publico = false;
    
    private String observacoes;

    private Long personalId;

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

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    public Long getPersonalId() {
        return personalId;
    }

    public void setPersonalId(Long personalId) {
        this.personalId = personalId;
    }
}
