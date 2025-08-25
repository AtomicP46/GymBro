package com.gymbro.dto;

import com.gymbro.model.Equipamento;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class EquipamentoDTO {

    private Long id;

    @NotBlank(message = "Nome do equipamento é obrigatório")
    private String nome;

    @NotNull(message = "Peso do equipamento é obrigatório")
    @Min(value = 0, message = "Peso deve ser maior ou igual a zero")
    private Float pesoEquip;

    public EquipamentoDTO() {
    }

    public EquipamentoDTO(Long id, String nome, Float pesoEquip) {
        this.id = id;
        this.nome = nome;
        this.pesoEquip = pesoEquip;
    }

    public EquipamentoDTO(String nome, Float pesoEquip) {
        this.nome = nome;
        this.pesoEquip = pesoEquip;
    }

    public Long getId() {
        return id;
    }

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

    /**
     * Converte este DTO para a entidade Equipamento.
     * Se id for nulo, a entidade será entendida como nova;
     * caso contrário, será usada para atualização.
     */
    public Equipamento toEntity() {
        if (id != null) {
            return new Equipamento(id, nome, pesoEquip);
        }
        return new Equipamento(nome, pesoEquip);
    }

    /**
     * Cria um DTO a partir de uma entidade Equipamento.
     */
    public static EquipamentoDTO fromEntity(Equipamento equipamento) {
        return new EquipamentoDTO(
            equipamento.getId(),
            equipamento.getNome(),
            equipamento.getPesoEquip()
        );
    }
}