package com.exemplo.modelo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Desativando CSRF para permitir requisições externas
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/login").permitAll() // Login liberado para todos
                        .requestMatchers("/clientes/**").hasRole("ADMIN") // Apenas ADMIN pode acessar clientes
                        .anyRequest().authenticated() // Demais rotas exigem autenticação
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)); // Sem estado para compatibilidade com JWT

        return http.build();
    }
}
