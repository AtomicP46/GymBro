package com.gymbro.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class TreinoDTO {
    private Long id;

    @NotBlank(message = "Nome do treino é obrigatório")
    private String nome;
    
    private String descricao;
    
    private LocalDateTime dataHoraInicio;
    private LocalDateTime dataHoraFim;
    
    @NotNull(message = "ID do usuário é obrigatório")
    private Long usuarioId;
    
    private Long personalId;

    // Construtores
    public TreinoDTO() {}

    public TreinoDTO(String nome, LocalDateTime dataHoraInicio, Long usuarioId, Long personalId) {
        this.nome = nome;
        this.dataHoraInicio = dataHoraInicio;
        this.usuarioId = usuarioId;
        this.personalId = personalId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
}
