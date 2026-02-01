package pweb.aula2909.model.security;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class GeradorSenha {

    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        System.out.println("admin -> " + encoder.encode("admin"));
        System.out.println("cliente   -> " + encoder.encode("123"));
    }
}
