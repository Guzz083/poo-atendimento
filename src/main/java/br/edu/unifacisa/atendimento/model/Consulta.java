package br.edu.unifacisa.atendimento.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Consulta guarda os OBJETOS Paciente e Medico (associação), não apenas os ids.
 * RN02: paciente tem perfil paciente e médico tem perfil médico (garantido pelos tipos);
 * data e hora obrigatórias; status inicial AGENDADA.
 */
public class Consulta {

    private static final DateTimeFormatter FORMATO =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private int id;
    private Paciente paciente;
    private Medico medico;
    private LocalDateTime dataHora;
    private StatusConsulta status = StatusConsulta.AGENDADA;
    private String observacoes;

    public Consulta() {
    }

    public Consulta(Paciente paciente, Medico medico, LocalDateTime dataHora) {
        setPaciente(paciente);
        setMedico(medico);
        setDataHora(dataHora);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        if (id < 0) {
            throw new IllegalArgumentException("O id não pode ser negativo.");
        }
        this.id = id;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        if (paciente == null) {
            throw new IllegalArgumentException("A consulta precisa de um paciente.");
        }
        this.paciente = paciente;
    }

    public Medico getMedico() {
        return medico;
    }

    public void setMedico(Medico medico) {
        if (medico == null) {
            throw new IllegalArgumentException("A consulta precisa de um médico.");
        }
        this.medico = medico;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        if (dataHora == null) {
            throw new IllegalArgumentException("A data e hora da consulta são obrigatórias.");
        }
        this.dataHora = dataHora;
    }

    public StatusConsulta getStatus() {
        return status;
    }

    public void setStatus(StatusConsulta status) {
        if (status == null) {
            throw new IllegalArgumentException("O status é obrigatório.");
        }
        this.status = status;
    }

    public String getObservacoes() {
        return observacoes;
    }

    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
    }

    @Override
    public String toString() {
        return String.format("Consulta #%d | %s | %s | Paciente: %s | Médico: %s (%s)%s",
                id,
                dataHora.format(FORMATO),
                status,
                paciente.getNome(),
                medico.getNome(),
                medico.getEspecialidade() == null ? "-" : medico.getEspecialidade(),
                observacoes == null || observacoes.isBlank() ? "" : " | Obs.: " + observacoes);
    }
}
