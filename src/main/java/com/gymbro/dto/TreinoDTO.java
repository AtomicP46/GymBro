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

    @NotNull(message = "ID do aluno é obrigatório")
    private Long alunoId;

    private Long personalId;

    public TreinoDTO() {}

    public TreinoDTO(String nome,
                     LocalDateTime dataHoraInicio,
                     Long alunoId,
                     Long personalId) {
        this.nome = nome;
        this.dataHoraInicio = dataHoraInicio;
        this.alunoId = alunoId;
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

    public Long getAlunoId() {
        return alunoId;
    }

    public void setAlunoId(Long alunoId) {
        this.alunoId = alunoId;
    }

    public Long getPersonalId() {
        return personalId;
    }

    public void setPersonalId(Long personalId) {
        this.personalId = personalId;
    }
}