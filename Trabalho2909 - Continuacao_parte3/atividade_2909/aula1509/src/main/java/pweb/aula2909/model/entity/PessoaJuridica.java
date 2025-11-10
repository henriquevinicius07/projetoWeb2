package pweb.aula2909.model.entity;

import jakarta.persistence.Entity;

import java.io.Serializable;

@Entity
public class PessoaJuridica extends Pessoa implements Serializable {

    private String razaoSocial;
    private String cnpj;

    public String getNomeExibicao() {
        return razaoSocial;
    }

    public String getRazaoSocial() {
        return razaoSocial;
    }

    public void setRazaoSocial(String razaoSocial) {
        this.razaoSocial = razaoSocial;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }
}
