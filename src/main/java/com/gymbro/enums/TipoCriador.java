package com.gymbro.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum TipoCriador {
    USUARIO("Usuario"),
    PERSONAL("Personal");

    private final String descricao;

    TipoCriador(String descricao) {
        this.descricao = descricao;
    }

    @JsonValue
    public String getDescricao() {
        return descricao;
    }

    @Override
    public String toString() {
        return descricao;
    }

    @JsonCreator
    public static TipoCriador fromString(String texto) {
        if (texto == null) {
            throw new IllegalArgumentException("Tipo de criador não pode ser nulo");
        }
        for (TipoCriador tipo : values()) {
            if (tipo.descricao.equalsIgnoreCase(texto.trim())) {
                return tipo;
            }
        }
        throw new IllegalArgumentException("Tipo de criador não encontrado: " + texto);
    }

    public static String[] getDescricoes() {
        String[] descricoes = new String[values().length];
        for (int i = 0; i < values().length; i++) {
            descricoes[i] = values()[i].getDescricao();
        }
        return descricoes;
    }

    public static boolean isValido(String texto) {
        try {
            fromString(texto);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}