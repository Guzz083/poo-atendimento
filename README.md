# Banco de Dados do Atendimento Médico com POO — 1ª entrega

Aplicação Java 17 (Maven) que se conecta ao MySQL `atendimento_medico` por JDBC, lê os usuários e as
consultas e os transforma em objetos das classes de domínio. O mesmo banco é usado pela API REST.

```
poo-atendimento/
├── banco/schema.sql          cria o banco e as tabelas (seção 3.1)
├── banco/dados.sql           1 secretária, 2 médicos, 3 pacientes e 4 consultas
├── docs/diagrama-classes.png diagrama de classes UML
├── pom.xml                   dependência do MySQL Connector/J
└── src/main/
    ├── resources/db.properties.example
    └── java/br/edu/unifacisa/atendimento/
        ├── model/  Usuario, Medico, Secretaria, Paciente, Consulta, Perfil, StatusConsulta
        ├── db/     ConexaoBD
        └── Main.java
```

## Como executar
1. **Banco:** no MySQL Workbench, abra e execute `banco/schema.sql` e depois `banco/dados.sql`
   (File → Open SQL Script → raio ⚡). Ou no terminal:
   ```
   mysql -u root -p < banco/schema.sql
   mysql -u root -p atendimento_medico < banco/dados.sql
   ```
2. **Configuração:** copie `src/main/resources/db.properties.example` para
   `src/main/resources/db.properties` e preencha `db.senha`. Esse arquivo está no `.gitignore`.
3. **Executar:** abra a pasta como projeto Maven na IDE e rode `Main`, ou no terminal:
   ```
   mvn compile exec:java -Dexec.mainClass=br.edu.unifacisa.atendimento.Main
   ```

## O que a execução mostra
1. **Validações nos setters (RN01):** senha curta, e-mail inválido, nome em branco e CPF inválido sendo recusados
   com `IllegalArgumentException`, e um e-mail sendo guardado em minúsculas.
2. **RF03:** `Conexão com o banco realizada com sucesso!` — ou `Erro ao conectar com o banco de dados: ...`
   (teste colocando uma senha errada no `db.properties`).
3. **RF04:** os usuários (como `Medico`, `Secretaria` ou `Paciente`, conforme a coluna `perfil`) e as consultas,
   impressos com `toString()`.
4. **Polimorfismo:** a mesma `List<Usuario>` chamando `getPerfil()` em objetos das três subclasses.

## Onde cada requisito foi atendido
| Ref. | Onde |
|---|---|
| RF01, RF02 | `banco/schema.sql`, `banco/dados.sql` |
| RF03 | `db/ConexaoBD.java` + `Main.main` |
| RF04 | `Main.lerUsuarios`, `Main.montarUsuario`, `Main.lerConsultas` |
| RN01 | setters de `Usuario` (nome, e-mail em minúsculas, senha ≥ 6) e enum `Perfil`; `Paciente.setCpf` valida o CPF |
| RN02 | `Consulta`: tipos `Paciente`/`Medico`, `dataHora` obrigatória, status inicial `AGENDADA` |
| RNF01 | `PreparedStatement` em todas as consultas SQL |
| RNF02 | try-with-resources em `Connection`, `PreparedStatement` e `ResultSet` |
| RNF03 | `db.properties` no `.gitignore`; `db.properties.example` versionado |
| RNF04 | `schema.sql` idêntico ao da API REST |

| Conceito (3.3) | Onde |
|---|---|
| Encapsulamento | atributos `private` + setters que validam e lançam `IllegalArgumentException` |
| Herança | `Medico`, `Secretaria` e `Paciente` estendem `Usuario` |
| Abstração | `Usuario` é abstrata, com `getPerfil()` abstrato |
| Polimorfismo | `getPerfil()` e `toString()` sobrescritos; `List<Usuario>` com as três subclasses |
| Associação | `Consulta` guarda objetos `Paciente` e `Medico` |
| Enumerações | `Perfil` e `StatusConsulta`, com `getValorBanco()` e `deValorBanco()` |

`Dao<T>`, DAOs, `ConsultaService`, exceções próprias, menu e testes JUnit são da 2ª entrega.

## Problemas comuns
- `Access denied for user 'root'` → senha errada no `db.properties`.
- `Arquivo db.properties não encontrado` → o arquivo precisa estar em `src/main/resources/`.
- `Public Key Retrieval is not allowed` → acrescente à URL:
  `?allowPublicKeyRetrieval=true&useSSL=false`.
