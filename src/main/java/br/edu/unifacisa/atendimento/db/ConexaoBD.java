package br.edu.unifacisa.atendimento.db;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Lê as credenciais do arquivo db.properties (src/main/resources, fora do Git — RNF03)
 * e devolve uma Connection JDBC.
 */
public final class ConexaoBD {

    private static final String ARQUIVO = "db.properties";
    private static Properties props;

    private ConexaoBD() {
    }

    /** Abre uma nova conexão com o banco. Quem chama fecha com try-with-resources. */
    public static Connection getConnection() throws SQLException {
        Properties p = carregarPropriedades();
        return DriverManager.getConnection(
                p.getProperty("db.url"),
                p.getProperty("db.usuario"),
                p.getProperty("db.senha"));
    }

    private static synchronized Properties carregarPropriedades() {
        if (props != null) {
            return props;
        }
        Properties p = new Properties();
        try (InputStream in = ConexaoBD.class.getClassLoader().getResourceAsStream(ARQUIVO)) {
            if (in == null) {
                throw new IllegalStateException("Arquivo " + ARQUIVO + " não encontrado em "
                        + "src/main/resources. Copie o db.properties.example para db.properties "
                        + "e preencha a senha.");
            }
            p.load(in);
        } catch (IOException e) {
            throw new IllegalStateException("Não foi possível ler o " + ARQUIVO + ".", e);
        }
        for (String chave : new String[]{"db.url", "db.usuario", "db.senha"}) {
            if (p.getProperty(chave) == null) {
                throw new IllegalStateException("Falta a chave '" + chave + "' no " + ARQUIVO + ".");
            }
        }
        props = p;
        return props;
    }
}
