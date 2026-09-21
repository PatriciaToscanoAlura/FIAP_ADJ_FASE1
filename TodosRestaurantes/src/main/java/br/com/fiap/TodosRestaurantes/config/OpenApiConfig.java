package br.com.fiap.TodosRestaurantes.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@OpenAPIDefinition
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI todosRestaurante() {
        return new OpenAPI()
                .info(
                        new Info().title("Todos Restaurante API")
                                .description("Sistema de Gestão de Restaurantes e Delivery")
                                .version("v0.0.1")
                                .license(new License().name("Apache 2.0").url("https://github.com/patriciatoscanoalura/fiap_adj_fase1"))
                );
    }

    @PropertySource("classpath:application.properties") // Garante que o Spring vai ler o arquivo
    public static class AppConfig {

        @Bean
        public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
            return new PropertySourcesPlaceholderConfigurer();
        }
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
