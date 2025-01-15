package com.example.library.config;

import com.example.library.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;

    public SecurityConfig(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    @SuppressWarnings({ "removal", "deprecation" })
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeRequests(authorizeRequests ->
                authorizeRequests
                    .requestMatchers("/login", "/register", "/h2-console/**").permitAll()  // Dopuszczamy dostęp do loginu, rejestracji i konsoli H2
                    .requestMatchers("/admin/**").hasRole("ADMIN")  // Tylko admini mogą uzyskać dostęp do zasobów /admin/**
                    .anyRequest().authenticated()  // Reszta aplikacji wymaga autentykacji
            )
            .formLogin(formLogin ->
                formLogin
                    .loginPage("/login")
                    .defaultSuccessUrl("/welcome", true)
                    .permitAll()
            )
            .logout(logout ->
                logout.permitAll()
            )
            .csrf(csrf -> csrf.disable())  // Wyłączenie CSRF dla konsoli H2
            .headers(headers -> headers.frameOptions().disable());  // Pozwolenie na osadzenie ramki w przeglądarce (potrzebne dla H2)

        return http.build();
    }

    // Dodanie serwisu UserDetailsService
    @Bean
    public UserDetailsService userDetailsService() {
        return customUserDetailsService;
    }

    // Dodanie kodowania haseł
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}






