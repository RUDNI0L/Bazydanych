package com.example.library.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @SuppressWarnings({ "removal", "deprecation" })
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .requestMatchers("/login", "/register", "/h2-console/**").permitAll()  
                .requestMatchers("/welcomeAdmin", "/editBook").hasAuthority("ROLE_ADMIN")  // Sprawdzanie roli z prefiksem "ROLE_"
                .requestMatchers("/welcome").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")
                .anyRequest().authenticated() 
            .and()
            .formLogin()
                .loginPage("/login")
                .defaultSuccessUrl("/welcome", true)
                .permitAll()
            .and()
            .logout()
                .permitAll()
            .and()
            .csrf().disable()
            .headers(headers -> headers.frameOptions().disable()); 

        return http.build();
    }
}
