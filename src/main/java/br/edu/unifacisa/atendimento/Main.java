package br.edu.unifacisa.atendimento;

import br.edu.unifacisa.atendimento.db.ConexaoBD;
import br.edu.unifacisa.atendimento.model.Consulta;
import br.edu.unifacisa.atendimento.model.Medico;
import br.edu.unifacisa.atendimento.model.Paciente;
import br.edu.unifacisa.atendimento.model.Perfil;
import br.edu.unifacisa.atendimento.model.Secretaria;
import br.edu.unifacisa.atendimento.model.StatusConsulta;
import br.edu.unifacisa.atendimento.model.Usuario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 1ª entrega.
 * RF03 - conecta ao banco por JDBC e mostra mensagem de sucesso ou de erro.
 * RF04 - lê usuários e consultas (SELECT simples), cria os objetos das classes
 *        de domínio e os exibe com toString().
 */
public class Main {

    private static final String SQL_USUARIOS =
            "SELECT id, nome, email, senha, perfil, crm, especialidade, cpf, telefone, criado_em "
            + "FROM usuarios ORDER BY id";

    private static final String SQL_CONSULTAS =
            "SELECT id, paciente_id, medico_id, data_hora, status, observacoes "
            + "FROM consultas ORDER BY data_hora";

    public static void main(String[] args) {
        demonstrarValidacoes();

        // RNF02: Connection, PreparedStatement e ResultSet fechados com try-with-resources
        try (Connection conexao = ConexaoBD.getConnection()) {
            System.out.println("\nConexão com o banco realizada com sucesso! (banco: "
                    + conexao.getCatalog() + ")");

            List<Usuario> usuarios = lerUsuarios(conexao);
            System.out.println("\n=== USUÁRIOS (" + usuarios.size() + ") ===");
            for (Usuario u : usuarios) {
                // Polimorfismo: cada objeto chama o toString() da própria subclasse
                System.out.println(u);
            }

            System.out.println("\n=== POLIMORFISMO: getPerfil() em uma List<Usuario> ===");
            for (Usuario u : usuarios) {
                System.out.printf("%-22s -> classe %-10s -> getPerfil() = %s (banco: '%s')%n",
                        u.getNome(), u.getClass().getSimpleName(),
                        u.getPerfil(), u.getPerfil().getValorBanco());
            }

            List<Consulta> consultas = lerConsultas(conexao, usuarios);
            System.out.println("\n=== CONSULTAS (" + consultas.size() + ") ===");
            for (Consulta c : consultas) {
                System.out.println(c);
            }
        } catch (SQLException e) {
            System.err.println("\nErro ao conectar com o banco de dados: " + e.getMessage());
        } catch (IllegalStateException e) {
            System.err.println("\nErro de configuração: " + e.getMessage());
        }
    }

    /** Mostra os setters recusando dados inválidos (encapsulamento com validação — RN01). */
    private static void demonstrarValidacoes() {
        System.out.println("=== VALIDAÇÕES NOS SETTERS (RN01) ===");
        tentar("Senha com menos de 6 caracteres",
                () -> new Secretaria(0, "Teste", "teste@clinica.com", "123"));
        tentar("E-mail inválido",
                () -> new Secretaria(0, "Teste", "email-sem-arroba", "123456"));
        tentar("Nome em branco",
                () -> new Secretaria(0, "   ", "teste@clinica.com", "123456"));
        tentar("CPF inválido",
                () -> new Paciente(0, "Teste", "teste@email.com", "123456", "11111111111", null));

        Secretaria ok = new Secretaria(0, "Teste", "  TESTE@Clinica.COM ", "123456");
        System.out.println("[OK] E-mail guardado em minúsculas: '" + ok.getEmail() + "'");
    }

    private static void tentar(String caso, Runnable acao) {
        try {
            acao.run();
            System.out.println("[FALHOU] " + caso + ": deveria ter sido recusado");
        } catch (IllegalArgumentException e) {
            System.out.println("[RECUSADO] " + caso + " -> " + e.getMessage());
        }
    }

    /** Lê a tabela usuarios e cria Medico, Secretaria ou Paciente conforme a coluna perfil. */
    private static List<Usuario> lerUsuarios(Connection conexao) throws SQLException {
        List<Usuario> usuarios = new ArrayList<>();
        try (PreparedStatement ps = conexao.prepareStatement(SQL_USUARIOS);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                try {
                    usuarios.add(montarUsuario(rs));
                } catch (IllegalArgumentException e) {
                    System.err.println("Usuário id " + rs.getInt("id") + " ignorado: " + e.getMessage());
                }
            }
        }
        return usuarios;
    }

    private static Usuario montarUsuario(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String nome = rs.getString("nome");
        String email = rs.getString("email");
        String senha = rs.getString("senha");

        Usuario usuario = switch (Perfil.deValorBanco(rs.getString("perfil"))) {
            case MEDICO -> new Medico(id, nome, email, senha,
                    rs.getString("crm"), rs.getString("especialidade"));
            case SECRETARIA -> new Secretaria(id, nome, email, senha);
            case PACIENTE -> new Paciente(id, nome, email, senha,
                    rs.getString("cpf"), rs.getString("telefone"));
        };

        Timestamp criadoEm = rs.getTimestamp("criado_em");
        if (criadoEm != null) {
            usuario.setCriadoEm(criadoEm.toLocalDateTime());
        }
        return usuario;
    }

    /**
     * Lê a tabela consultas e associa cada uma aos OBJETOS Paciente e Medico
     * já carregados (associação, não apenas ids).
     */
    private static List<Consulta> lerConsultas(Connection conexao, List<Usuario> usuarios)
            throws SQLException {
        Map<Integer, Usuario> porId = new HashMap<>();
        for (Usuario u : usuarios) {
            porId.put(u.getId(), u);
        }

        List<Consulta> consultas = new ArrayList<>();
        try (PreparedStatement ps = conexao.prepareStatement(SQL_CONSULTAS);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("id");
                try {
                    // RN02: os tipos garantem que o paciente é Paciente e o médico é Medico
                    if (!(porId.get(rs.getInt("paciente_id")) instanceof Paciente paciente)) {
                        throw new IllegalArgumentException("paciente_id não é de um paciente.");
                    }
                    if (!(porId.get(rs.getInt("medico_id")) instanceof Medico medico)) {
                        throw new IllegalArgumentException("medico_id não é de um médico.");
                    }
                    Timestamp dataHora = rs.getTimestamp("data_hora");
                    Consulta c = new Consulta(paciente, medico,
                            dataHora == null ? null : dataHora.toLocalDateTime());
                    c.setId(id);
                    c.setStatus(StatusConsulta.deValorBanco(rs.getString("status")));
                    c.setObservacoes(rs.getString("observacoes"));
                    consultas.add(c);
                } catch (IllegalArgumentException e) {
                    System.err.println("Consulta id " + id + " ignorada: " + e.getMessage());
                }
            }
        }
        return consultas;
    }
}
