package pweb.aula2909.model.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import pweb.aula2909.model.service.UsuarioService;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;


import static org.springframework.security.config.Customizer.withDefaults;

@Configuration //classe de configuração
@EnableWebSecurity //indica ao Spring que serão definidas configurações personalizadas de segurança
public class SecurityConfiguration {

    @Autowired
    private UsuarioService usuarioService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(
                        customizer ->
                                customizer
                                        // H2 Console - Acesso público
                                        .requestMatchers("/h2-console/**").permitAll()

                                        // Rotas públicas (sem autenticação)
                                        .requestMatchers("/login").permitAll()
                                        .requestMatchers("/escolher-tipo-cadastro").permitAll()
                                        .requestMatchers("/produto/listVenda").permitAll()
                                        .requestMatchers("/pessoafisica/form").permitAll()
                                        .requestMatchers("/pessoajuridica/form").permitAll()
                                        .requestMatchers(HttpMethod.POST, "/pessoafisica/salvar").permitAll()
                                        .requestMatchers(HttpMethod.POST, "/pessoajuridica/salvar").permitAll()
                                        .requestMatchers(HttpMethod.POST, "/produto/form/salvar").hasAnyRole("ADMIN")
                                        .requestMatchers("/images/**").permitAll()

                                        .requestMatchers("/venda/list").permitAll() // Venda disponível para todos

                                        // Rotas para CLIENTE autenticado
                                        .requestMatchers("/carrinho/carrinho").hasAnyRole("CLIENTE", "ADMIN")

                                        // Rotas que exigem ADMIN
                                        .requestMatchers("/pessoafisica/list").hasAnyRole("ADMIN")
                                        .requestMatchers("/pessoafisica/editar/**").hasAnyRole("ADMIN")
                                        .requestMatchers("/pessoajuridica/list").hasAnyRole("ADMIN")
                                        .requestMatchers("/pessoajuridica/editar/**").hasAnyRole("ADMIN")
                                        .requestMatchers("/produto/list").hasAnyRole("ADMIN")

                                        // Qualquer outra requisição requer autenticação
                                        .anyRequest() //define que a configuração é válida para qualquer requisição.
                                        .authenticated()//define que o usuário precisa estar autenticado.
                )
                .formLogin(customizer ->
                        customizer
                                .loginPage("/login") //passamos como parâmetro a URL para acesso à página de login que criamos
                                .defaultSuccessUrl("/produto/listVenda", true)
                                .permitAll() //define que essa página pode ser acessada por todos, independentemente do usuário estar autenticado ou não.
                )
                .httpBasic(withDefaults()) //configura a autenticação básica (usuário e senha)
                .logout(LogoutConfigurer::permitAll) //configura a funcionalidade de logout no Spring Security.
                .rememberMe(customizer -> customizer.userDetailsService(usuarioService)) //permite que os usuários permaneçam autenticados mesmo após o fechamento do navegador
                .csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**")) // Desabilitar CSRF para H2 console
                .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable())); // Permitir frames para H2
                 http.authenticationProvider(authenticationProvider());

        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(usuarioService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}