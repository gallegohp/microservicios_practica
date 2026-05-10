package com.gallego.ms_auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuración de Seguridad de Spring
 * 
 * Esta clase permite acceso público a /auth/** (register, login, refresh)
 * y protege todos los demás endpoints con autenticación
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Desactivar protección CSRF (no necesaria para API REST sin cookies)
            .csrf(csrf -> csrf.disable())
            // No crear sesiones - cada request es independiente (stateless)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            // Configurar accesos:
            .authorizeHttpRequests(auth -> auth
                // /auth/** - cualquiera puede acceder (register, login, refresh)
                .requestMatchers("/auth/**").permitAll()
                // Cualquier otra ruta - requiere autenticación
                .anyRequest().authenticated()
            );
        return http.build();
    }
}