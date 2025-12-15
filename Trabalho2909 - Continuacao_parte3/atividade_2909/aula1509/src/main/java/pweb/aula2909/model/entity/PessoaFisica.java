package pweb.aula2909.model.entity;

import jakarta.persistence.Entity;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.io.Serializable;


@Entity
public class PessoaFisica extends Pessoa implements Serializable {

    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 3, max = 100, message = "Nome deve ter entre 3 e 100 caracteres")
    private String nome;
    @NotBlank(message = "CPF é obrigatório")
    @Pattern(regexp = "\\d{11}", message = "CPF deve conter 11 dígitos")
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
