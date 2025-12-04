package com.rafaelnobel.researchtracker.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // 1. MATIKAN CSRF (Supaya form upload & login tidak ditolak)
            .csrf(csrf -> csrf.disable()) 
            
            // 2. IZINKAN SEMUA REQUEST (Supaya tidak ada redirect otomatis yang mengganggu)
            // Keamanan sekarang sepenuhnya dikontrol oleh "if (session == null)" di Controller kamu.
            .authorizeHttpRequests(auth -> auth
                .anyRequest().permitAll() 
            );

        return http.build();
    }

    // 3. PASSWORD ENCODER (Wajib ada agar aplikasi tidak crash saat start)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
}