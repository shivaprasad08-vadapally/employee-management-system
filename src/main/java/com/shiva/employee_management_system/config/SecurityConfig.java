package com.shiva.employee_management_system.config;

import com.shiva.employee_management_system.security.JwtAuthenticationFilter;
import com.shiva.employee_management_system.security.SecurityExceptionHandler;
import com.shiva.employee_management_system.service.CustomUserDetailsService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.http.HttpMethod;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final SecurityExceptionHandler securityExceptionHandler;

    public SecurityConfig(
            JwtAuthenticationFilter jwtAuthenticationFilter,
            SecurityExceptionHandler securityExceptionHandler) {

        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.securityExceptionHandler = securityExceptionHandler;
    }

    // =========================================================
    // PASSWORD ENCODER
    // =========================================================

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // =========================================================
    // AUTHENTICATION PROVIDER
    // =========================================================

    @Bean
    public DaoAuthenticationProvider authenticationProvider(
            CustomUserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder) {

        DaoAuthenticationProvider provider =
                new DaoAuthenticationProvider(userDetailsService);

        provider.setPasswordEncoder(passwordEncoder);

        return provider;
    }

    // =========================================================
    // AUTHENTICATION MANAGER
    // =========================================================

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration configuration)
            throws Exception {

        return configuration.getAuthenticationManager();
    }

    // =========================================================
    // SECURITY FILTER CHAIN
    // =========================================================

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http)
            throws Exception {

        http

            // Disable CSRF because we use JWT
            .csrf(csrf -> csrf.disable())

            // JWT authentication does not use HTTP sessions
            .sessionManagement(session ->
                    session.sessionCreationPolicy(
                            SessionCreationPolicy.STATELESS
                    )
            )

            // =================================================
            // AUTHORIZATION RULES
            // =================================================

            .authorizeHttpRequests(auth -> auth

                // -----------------------------
                // PUBLIC ENDPOINTS
                // -----------------------------

                .requestMatchers(
                        "/swagger-ui/**",
                        "/v3/api-docs/**",
                        "/api/auth/login"
                ).permitAll()

                // -----------------------------
                // GET → ADMIN + USER
                // -----------------------------

                .requestMatchers(
                        HttpMethod.GET,
                        "/api/employees/**"
                ).hasAnyRole("ADMIN", "USER")

                // -----------------------------
                // POST → ADMIN ONLY
                // -----------------------------

                .requestMatchers(
                        HttpMethod.POST,
                        "/api/employees/**"
                ).hasRole("ADMIN")

                // -----------------------------
                // PUT → ADMIN ONLY
                // -----------------------------

                .requestMatchers(
                        HttpMethod.PUT,
                        "/api/employees/**"
                ).hasRole("ADMIN")

                // -----------------------------
                // DELETE → ADMIN ONLY
                // -----------------------------

                .requestMatchers(
                        HttpMethod.DELETE,
                        "/api/employees/**"
                ).hasRole("ADMIN")

                // -----------------------------
                // EVERYTHING ELSE
                // -----------------------------

                .anyRequest().authenticated()
            )

            // =================================================
            // 401 / 403 HANDLING
            // =================================================

            .exceptionHandling(exception -> exception

                // 401 Unauthorized
                .authenticationEntryPoint(
                        securityExceptionHandler
                )

                // 403 Forbidden
                .accessDeniedHandler(
                        securityExceptionHandler
                )
            )

            // =================================================
            // JWT FILTER
            // =================================================

            .addFilterBefore(
                    jwtAuthenticationFilter,
                    UsernamePasswordAuthenticationFilter.class
            );

        return http.build();
    }
}