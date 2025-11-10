package pweb.aula2909.model.entity;

import jakarta.persistence.Entity;

import java.io.Serializable;


@Entity
public class PessoaFisica extends Pessoa implements Serializable {

    private String nome;
    private String cpf;

    public String getNomeExibicao() {
        return nome;
    }


    public String getNome() {
        return nome;
    }

    public void setNome(String nome)     {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }
}
