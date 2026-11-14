package br.edu.unifacisa.atendimento.model;

public class Paciente extends Usuario {

    private String cpf;
    private String telefone;

    public Paciente() {
    }

    public Paciente(int id, String nome, String email, String senha,
                    String cpf, String telefone) {
        super(id, nome, email, senha);
        setCpf(cpf);
        setTelefone(telefone);
    }

    @Override
    public Perfil getPerfil() {
        return Perfil.PACIENTE;
    }

    public String getCpf() {
        return cpf;
    }

    /**
     * Aceita o CPF com ou sem pontuação e guarda só os 11 dígitos (CHAR(11) no banco).
     * A coluna aceita NULL (o paciente pode ter sido cadastrado pela API sem CPF),
     * mas, quando informado, o CPF precisa ser válido.
     */
    public void setCpf(String cpf) {
        if (cpf == null || cpf.isBlank()) {
            this.cpf = null;
            return;
        }
        String digitos = cpf.replaceAll("\\D", "");
        if (!cpfValido(digitos)) {
            throw new IllegalArgumentException("CPF inválido: " + cpf);
        }
        this.cpf = digitos;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        if (telefone != null && telefone.length() > 20) {
            throw new IllegalArgumentException("O telefone deve ter no máximo 20 caracteres.");
        }
        this.telefone = telefone;
    }

    /** Confere os 11 dígitos e os dois dígitos verificadores. */
    static boolean cpfValido(String d) {
        if (d.length() != 11 || d.chars().distinct().count() == 1) {
            return false;
        }
        for (int pos = 9; pos <= 10; pos++) {
            int soma = 0;
            for (int i = 0; i < pos; i++) {
                soma += (d.charAt(i) - '0') * (pos + 1 - i);
            }
            int dv = (soma * 10) % 11 % 10;
            if (dv != d.charAt(pos) - '0') {
                return false;
            }
        }
        return true;
    }

    /** CPF formatado: 123.456.789-09 */
    public String getCpfFormatado() {
        if (cpf == null) {
            return "-";
        }
        return cpf.substring(0, 3) + "." + cpf.substring(3, 6) + "."
                + cpf.substring(6, 9) + "-" + cpf.substring(9);
    }

    @Override
    public String toString() {
        return super.toString() + String.format(" | CPF: %s | Telefone: %s",
                getCpfFormatado(), telefone == null ? "-" : telefone);
    }
}
