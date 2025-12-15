package pweb.aula2909.model.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import java.io.Serializable;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Pessoa implements Serializable {

    @Id
    @GeneratedValue(generator = "inc")
    @GenericGenerator(name = "inc", strategy = "increment")
    private Long id;

    @Email(message = "E-mail inválido")
    @NotBlank(message = "E-mail é obrigatório")
    private String email;
    @NotBlank(message = "Telefone é obrigatório")
    private String telefone;

    public abstract String getNomeExibicao();

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public boolean tipoObjeto(String objeto){
        return this.getClass().getSimpleName().toLowerCase().equals(objeto.toLowerCase());
    }

    public String getNomeClasse() {
        return (this instanceof PessoaFisica) ? "pessoafisica" : "pessoajuridica";
    }

    public String nomeClasse(){
        return this.getClass().getSimpleName().toLowerCase();
    }
}
