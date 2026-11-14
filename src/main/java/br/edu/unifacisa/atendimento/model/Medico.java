package br.edu.unifacisa.atendimento.model;

public class Medico extends Usuario {

    private String crm;
    private String especialidade;

    public Medico() {
    }

    public Medico(int id, String nome, String email, String senha,
                  String crm, String especialidade) {
        super(id, nome, email, senha);
        setCrm(crm);
        setEspecialidade(especialidade);
    }

    @Override
    public Perfil getPerfil() {
        return Perfil.MEDICO;
    }

    public String getCrm() {
        return crm;
    }

    public void setCrm(String crm) {
        if (crm == null || crm.isBlank()) {
            this.crm = null;   // a coluna aceita NULL; quando informado, é validado abaixo
            return;
        }
        if (crm.trim().length() > 20) {
            throw new IllegalArgumentException("O CRM deve ter no máximo 20 caracteres.");
        }
        this.crm = crm.trim();
    }

    public String getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(String especialidade) {
        if (especialidade != null && especialidade.length() > 80) {
            throw new IllegalArgumentException("A especialidade deve ter no máximo 80 caracteres.");
        }
        this.especialidade = especialidade;
    }

    @Override
    public String toString() {
        return super.toString() + String.format(" | CRM: %s | Especialidade: %s",
                crm == null ? "-" : crm, especialidade == null ? "-" : especialidade);
    }
}
