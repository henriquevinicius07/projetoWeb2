package pweb.aula2909.model.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import pweb.aula2909.model.service.UsuarioService;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Autowired
    private UsuarioService usuarioService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(
                        customizer ->
                                customizer
                                        // H2 Console
                                        .requestMatchers("/h2-console/**").permitAll()

                                        // Públicas
                                        .requestMatchers("/login").permitAll()
                                        .requestMatchers("/escolher-tipo-cadastro").permitAll()
                                        .requestMatchers("/produto/listVenda").permitAll()
                                        .requestMatchers("/pessoafisica/form").permitAll()
                                        .requestMatchers("/pessoajuridica/form").permitAll()
                                        .requestMatchers(HttpMethod.POST, "/pessoafisica/salvar").permitAll()
                                        .requestMatchers(HttpMethod.POST, "/pessoajuridica/salvar").permitAll()
                                        .requestMatchers("/images/**").permitAll()

                                        // Carrinho (cliente/admin)
                                        .requestMatchers("/carrinho/**").hasAnyRole("CLIENTE", "ADMIN")

                                        // Vendas (cliente/admin) - controller filtra o que aparece
                                        .requestMatchers("/venda/**").hasAnyRole("CLIENTE", "ADMIN")

                                        // Admin
                                        .requestMatchers("/pessoafisica/list", "/pessoafisica/editar/**").hasRole("ADMIN")
                                        .requestMatchers("/pessoajuridica/list", "/pessoajuridica/editar/**").hasRole("ADMIN")
                                        .requestMatchers("/produto/list").hasRole("ADMIN")
                                        .requestMatchers(HttpMethod.POST, "/produto/form/salvar").hasRole("ADMIN")

                                        .anyRequest().authenticated()
                )
                .formLogin(customizer ->
                        customizer
                                .loginPage("/login")
                                .defaultSuccessUrl("/produto/listVenda", true)
                                .permitAll()
                )
                .httpBasic(withDefaults())
                .logout(LogoutConfigurer::permitAll)
                .rememberMe(customizer -> customizer.userDetailsService(usuarioService))
                .csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**"))
                .headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()));

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
