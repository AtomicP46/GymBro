package com.gymbro.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import com.gymbro.enums.TipoCriador;

@Entity
@Table(name = "plano")
public class Plano {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Nome do plano é obrigatório")
    @Column(nullable = false, length = 100)
    private String nome;
    
    @NotBlank(message = "Descrição do plano é obrigatória")
    @Column(nullable = false, length = 500)
    private String descricao;
    
    @NotNull(message = "ID do criador é obrigatório")
    @Column(name = "criador_id", nullable = false)
    private Long criadorId;
    
    @NotNull(message = "Tipo do criador é obrigatório")
    @Enumerated(EnumType.STRING)
    @Column(name = "tipo_criador", nullable = false)
    private TipoCriador tipoCriador;
    
    @Column(nullable = false)
    private Boolean publico = false;
    
    @Column(length = 1000)
    private String observacoes;
    
    @Column(name = "data_criacao", nullable = false, updatable = false)
    private LocalDateTime dataCriacao;

    @PrePersist
    protected void onCreate() {
        dataCriacao = LocalDateTime.now();
    }

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
