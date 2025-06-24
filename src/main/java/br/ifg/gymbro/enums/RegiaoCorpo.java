package br.ifg.gymbro.enums;

public enum RegiaoCorpo {
    CARDIO("Cardio"),
    ABDOMEN("Abdomen"),
    BICEPS("Biceps"),
    TRICEPS("Triceps"),
    PEITO("Peito"),
    COSTAS("Costas"),
    OMBRO("Ombro"),
    PERNAS("Pernas");

    private final String descricao;

    RegiaoCorpo(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    public static RegiaoCorpo fromString(String texto) {
        for (RegiaoCorpo regiao : RegiaoCorpo.values()) {
            if (regiao.descricao.equalsIgnoreCase(texto)) {
                return regiao;
            }
        }
        throw new IllegalArgumentException("Região não encontrada: " + texto);
    }

    public static String[] getDescricoes() {
        String[] descricoes = new String[values().length];
        for (int i = 0; i < values().length; i++) {
            descricoes[i] = values()[i].getDescricao();
        }
        return descricoes;
    }

    public static boolean isValida(String regiao) {
        try {
            fromString(regiao);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}

