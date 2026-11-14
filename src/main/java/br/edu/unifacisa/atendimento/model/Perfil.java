package br.edu.unifacisa.atendimento.model;

/** Perfis de usuário. Os valores são os mesmos do ENUM da coluna usuarios.perfil. */
public enum Perfil {
    MEDICO("medico"),
    SECRETARIA("secretaria"),
    PACIENTE("paciente");

    private final String valorBanco;

    Perfil(String valorBanco) {
        this.valorBanco = valorBanco;
    }

    /** Texto gravado no banco (ex.: "medico"). */
    public String getValorBanco() {
        return valorBanco;
    }

    /** Converte o texto da coluna para o enum. */
    public static Perfil deValorBanco(String valor) {
        for (Perfil p : values()) {
            if (p.valorBanco.equalsIgnoreCase(valor)) {
                return p;
            }
        }
        throw new IllegalArgumentException("Perfil desconhecido: " + valor);
    }
}
