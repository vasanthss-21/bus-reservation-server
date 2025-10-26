package com.SpringBoot.Bus_Reservation_System.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // Disable CSRF (Cross-Site Request Forgery) protection, which is not
        // needed for a stateless REST API that will be called by a frontend app.
        http.csrf(csrf -> csrf.disable());

        // Permit all requests to any endpoint without requiring authentication.
        // This allows your frontend to freely call GET, POST, DELETE, etc.
        http.authorizeHttpRequests(auth -> auth.anyRequest().permitAll());

        return http.build();
    }
}
