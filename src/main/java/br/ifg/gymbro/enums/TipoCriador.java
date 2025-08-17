package br.ifg.gymbro.enums;

public enum TipoCriador {
    USUARIO("Usuario"),
    PERSONAL("Personal");

    private final String descricao;

    TipoCriador(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    public static TipoCriador fromString(String texto) {
        for (TipoCriador tipo : TipoCriador.values()) {
            if (tipo.descricao.equalsIgnoreCase(texto)) {
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

    public static boolean isValido(String tipo) {
        try {
            fromString(tipo);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}

