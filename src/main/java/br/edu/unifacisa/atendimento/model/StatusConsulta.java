package br.edu.unifacisa.atendimento.model;

/** Situação da consulta. Os valores são os mesmos do ENUM da coluna consultas.status. */
public enum StatusConsulta {
    AGENDADA("agendada"),
    REALIZADA("realizada"),
    CANCELADA("cancelada");

    private final String valorBanco;

    StatusConsulta(String valorBanco) {
        this.valorBanco = valorBanco;
    }

    public String getValorBanco() {
        return valorBanco;
    }

    public static StatusConsulta deValorBanco(String valor) {
        for (StatusConsulta s : values()) {
            if (s.valorBanco.equalsIgnoreCase(valor)) {
                return s;
            }
        }
        throw new IllegalArgumentException("Status desconhecido: " + valor);
    }
}
