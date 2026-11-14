package br.edu.unifacisa.atendimento.model;

/** Secretária não tem atributos próprios; só informa o seu perfil. */
public class Secretaria extends Usuario {

    public Secretaria() {
    }

    public Secretaria(int id, String nome, String email, String senha) {
        super(id, nome, email, senha);
    }

    @Override
    public Perfil getPerfil() {
        return Perfil.SECRETARIA;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
