package com.gymbro.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "treinos")
public class Treino {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Nome do treino é obrigatório")
    @Column(nullable = false)
    private String nome;
    
    @Column(name = "data_hora_inicio")
    private LocalDateTime dataHoraInicio;
    
    @Column(name = "data_hora_fim")
    private LocalDateTime dataHoraFim;
    
    @NotNull(message = "ID do usuário é obrigatório")
    @Column(name = "usuario_id", nullable = false)
    private Long usuarioId;
    
    @Column(name = "personal_id")
    private Long personalId;
    
    // Para exibição - não persistidos
    @Transient
    private String usuarioNome;
    
    @Transient
    private String personalNome;

    // Construtores
    public Treino() {}

    public Treino(String nome, LocalDateTime dataHoraInicio, Long usuarioId, Long personalId) {
        this.nome = nome;
        this.dataHoraInicio = dataHoraInicio;
        this.usuarioId = usuarioId;
        this.personalId = personalId;
    }

    public Treino(Long id, String nome, LocalDateTime dataHoraInicio, LocalDateTime dataHoraFim, 
                  Long usuarioId, Long personalId) {
        this.id = id;
        this.nome = nome;
        this.dataHoraInicio = dataHoraInicio;
        this.dataHoraFim = dataHoraFim;
        this.usuarioId = usuarioId;
        this.personalId = personalId;
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

    public LocalDateTime getDataHoraInicio() {
        return dataHoraInicio;
    }

    public void setDataHoraInicio(LocalDateTime dataHoraInicio) {
        this.dataHoraInicio = dataHoraInicio;
    }

    public LocalDateTime getDataHoraFim() {
        return dataHoraFim;
    }

    public void setDataHoraFim(LocalDateTime dataHoraFim) {
        this.dataHoraFim = dataHoraFim;
    }

    public Long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public Long getPersonalId() {
        return personalId;
    }

    public void setPersonalId(Long personalId) {
        this.personalId = personalId;
    }

    public String getUsuarioNome() {
        return usuarioNome;
    }

    public void setUsuarioNome(String usuarioNome) {
        this.usuarioNome = usuarioNome;
    }

    public String getPersonalNome() {
        return personalNome;
    }

    public void setPersonalNome(String personalNome) {
        this.personalNome = personalNome;
    }

    // Métodos utilitários
    public boolean isIniciado() {
        return dataHoraInicio != null;
    }

    public boolean isFinalizado() {
        return dataHoraFim != null;
    }

    public boolean isEmAndamento() {
        return isIniciado() && !isFinalizado();
    }

    @Override
    public String toString() {
        return "Treino{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", dataHoraInicio=" + dataHoraInicio +
                ", dataHoraFim=" + dataHoraFim +
                ", usuarioId=" + usuarioId +
                ", personalId=" + personalId +
                '}';
    }
}
