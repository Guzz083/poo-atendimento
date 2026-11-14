package br.edu.unifacisa.atendimento.model;

import java.time.LocalDateTime;
import java.util.regex.Pattern;

/**
 * Classe base (abstrata) de todos os usuários do sistema.
 * RN01: nome obrigatório; e-mail obrigatório, válido e em minúsculas;
 * senha com no mínimo 6 caracteres; perfil definido pela subclasse.
 */
public abstract class Usuario {

    private static final Pattern EMAIL =
            Pattern.compile("^[\\w.+-]+@[\\w-]+(\\.[\\w-]+)+$");
    public static final int TAMANHO_MINIMO_SENHA = 6;

    private int id;
    private String nome;
    private String email;
    private String senha;          // no banco fica o hash BCrypt
    private LocalDateTime criadoEm;

    protected Usuario() {
    }

    protected Usuario(int id, String nome, String email, String senha) {
        setId(id);
        setNome(nome);
        setEmail(email);
        setSenha(senha);
    }

    /** Cada subclasse informa o seu perfil (polimorfismo). */
    public abstract Perfil getPerfil();

    // ---------- getters e setters com validação ----------

    public int getId() {
        return id;
    }

    public void setId(int id) {
        if (id < 0) {
            throw new IllegalArgumentException("O id não pode ser negativo.");
        }
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("O nome é obrigatório.");
        }
        this.nome = nome.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("O e-mail é obrigatório.");
        }
        String normalizado = email.trim().toLowerCase();
        if (!EMAIL.matcher(normalizado).matches()) {
            throw new IllegalArgumentException("E-mail inválido: " + email);
        }
        this.email = normalizado;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        if (senha == null || senha.length() < TAMANHO_MINIMO_SENHA) {
            throw new IllegalArgumentException(
                    "A senha deve ter no mínimo " + TAMANHO_MINIMO_SENHA + " caracteres.");
        }
        this.senha = senha;
    }

    public LocalDateTime getCriadoEm() {
        return criadoEm;
    }

    public void setCriadoEm(LocalDateTime criadoEm) {
        this.criadoEm = criadoEm;
    }

    @Override
    public String toString() {
        // A senha nunca aparece no toString.
        return String.format("[%s] #%d %s <%s>", getPerfil(), id, nome, email);
    }
}
