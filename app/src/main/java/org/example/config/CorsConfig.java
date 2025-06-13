package org.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/clientes/**") // Aplica CORS para todas as rotas de clientes
                        .allowedOrigins("*") // Permite qualquer origem (ajustável)
                        .allowedMethods("GET", "POST", "PUT", "DELETE"); // Define métodos permitidos
            }
        };
    }
}

