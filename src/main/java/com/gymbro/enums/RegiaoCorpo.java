package com.gymbro.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Arrays;

public enum RegiaoCorpo {
    CARDIO("Cardio"),
    ABDOMEN("Abdomen"),
    BICEPS("Bíceps"),
    TRICEPS("Tríceps"),
    PEITO("Peito"),
    COSTAS("Costas"),
    OMBRO("Ombro"),
    PERNAS("Pernas");

    private final String descricao;

    RegiaoCorpo(String descricao) {
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
    public static RegiaoCorpo fromString(String texto) {
        return Arrays.stream(values())
                     .filter(r -> r.descricao.equalsIgnoreCase(texto.trim()))
                     .findFirst()
                     .orElseThrow(() -> new IllegalArgumentException("Região não encontrada: " + texto));
    }

    public static String[] getDescricoes() {
        return Arrays.stream(values())
                     .map(RegiaoCorpo::getDescricao)
                     .toArray(String[]::new);
    }
}