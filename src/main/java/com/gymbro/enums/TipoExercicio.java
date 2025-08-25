package com.gymbro.enums;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import java.util.Arrays;

public enum TipoExercicio {
    FORCA_PESO_REPETICOES("Força Peso e Repetições"),
    FORCA_PESO_TEMPO("Força Peso e Tempo"),
    PESO_CORPO_COM_PESO_REPETICOES("Peso do Corpo com Peso e Repetições"),
    PESO_CORPO_REPETICOES("Peso do Corpo e Repetições"),
    PESO_CORPO_TEMPO("Peso do Corpo e Tempo"),
    PESO_CORPO_ASSISTENCIA_REPETICOES("Peso do Corpo com Assistência de Peso e Repetições"),
    CARDIO_TEMPO_DISTANCIA_CALORIAS("Cardio com Tempo, Distância e Calorias"),
    OUTROS("Outros");

    private final String descricao;

    TipoExercicio(String descricao) {
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
    public static TipoExercicio fromString(String texto) {
        return Arrays.stream(values())
                     .filter(t -> t.descricao.equalsIgnoreCase(texto.trim()))
                     .findFirst()
                     .orElseThrow(() -> new IllegalArgumentException("Tipo não encontrado: " + texto));
    }

    public static String[] getDescricoes() {
        return Arrays.stream(values())
                     .map(TipoExercicio::getDescricao)
                     .toArray(String[]::new);
    }
}