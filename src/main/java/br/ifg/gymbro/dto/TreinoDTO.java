package br.ifg.gymbro.dto;

import java.time.LocalDateTime;

public class TreinoDTO {
    private String nome;
    private LocalDateTime dataHoraInicio;
    private LocalDateTime dataHoraFim;
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

    // Getters e Setters
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
}

