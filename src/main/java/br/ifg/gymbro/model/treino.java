package br.ifg.gymbro.model;

import java.time.LocalDateTime;

public class Treino {
    private Long id;
    private String nome;
    private LocalDateTime dataHoraInicio;
    private LocalDateTime dataHoraFim;
    private Long usuarioId;
    private Long personalId;
    
    // Para exibição
    private String usuarioNome;
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
