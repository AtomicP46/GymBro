package br.ifg.gymbro.enums;

public enum TipoExercicio {
    FORCA_PESO_REPETICOES("Força Peso e Repetições"),
    FORCA_PESO_TEMPO("Força Peso e Tempo"),
    PESO_CORPO_COM_PESO_REPETICOES("Peso do Corpo com Peso e Repetições"),
    PESO_CORPO_REPETICOES("Peso do Corpo e Repetições"),
    PESO_CORPO_TEMPO("Peso do corpo e Tempo"),
    PESO_CORPO_ASSISTENCIA_REPETICOES("Peso do corpo com assistência de peso e repetições"),
    CARDIO_TEMPO_DISTANCIA_CALORIAS("Cardio com tempo distância e Calorias ou Kcal"),
    OUTROS("Outros");

    private final String descricao;

    TipoExercicio(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    public static TipoExercicio fromString(String texto) {
        for (TipoExercicio tipo : TipoExercicio.values()) {
            if (tipo.descricao.equalsIgnoreCase(texto)) {
                return tipo;
            }
        }
        throw new IllegalArgumentException("Tipo de exercício não encontrado: " + texto);
    }

    public static String[] getDescricoes() {
        String[] descricoes = new String[values().length];
        for (int i = 0; i < values().length; i++) {
            descricoes[i] = values()[i].getDescricao();
        }
        return descricoes;
    }

    public static boolean isValido(String tipo) {
        try {
            fromString(tipo);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}

