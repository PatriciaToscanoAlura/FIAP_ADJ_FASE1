package br.com.fiap.TodosRestaurantes.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class ConfiguracaoSeguranca {
    private final FiltroAutenticacao jwtFiltroAutenticacao;

    public ConfiguracaoSeguranca(FiltroAutenticacao jwtFiltroAutenticacao) {
        this.jwtFiltroAutenticacao = jwtFiltroAutenticacao;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // Desabilita CSRF (comum em APIs REST)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.POST, "/login", "/usuario").permitAll()
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui/index.html", "/h2-console/**").permitAll()// Rota de login liberada!
                        .anyRequest().authenticated() // QUALQUER OUTRA rota exige o token!
                )
                // Adiciona o nosso filtro JWT antes do filtro padrão de usuário/senha do Spring
                .addFilterBefore(jwtFiltroAutenticacao, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
