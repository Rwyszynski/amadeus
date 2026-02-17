package com.example.amdaeus.security;

import com.example.amdaeus.security.forFrontEnd.CustomLoginFailureHandler;
import com.example.amdaeus.security.forFrontEnd.CustomLoginSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, CustomLoginSuccessHandler successHandler,
                                           CustomLoginFailureHandler failureHandler) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth

                        .requestMatchers("/v1/users/*").hasAuthority("ADMIN_ROLE")
                        .requestMatchers("/threshold/*").hasAuthority("ADMIN_ROLE")
                        .requestMatchers("/admin/users/**").hasAuthority("ADMIN_ROLE")
                        .requestMatchers("/v1/users/*").hasAuthority("ADMIN_ROLE")

                        // Publiczne

                        .requestMatchers("/v1/auth/register", "/v1/auth/login").permitAll()
                        .requestMatchers("/register", "/login").permitAll()
                        // Każdy zalogowany użytkownik

                        .requestMatchers("/v1/auth/me").authenticated()

                        // Standard User

                        .requestMatchers(HttpMethod.POST, "/v1/btrs").hasAnyAuthority("STANDARD_ROLE", "C_LEVEL_ROLE")
                        .requestMatchers(HttpMethod.GET, "/v1/btrs/my").hasAnyAuthority("STANDARD_ROLE", "C_LEVEL_ROLE")

                        // Approver
                        .requestMatchers(HttpMethod.GET, "/v1/btrs/pending").hasAuthority("APPROVER_ROLE")
                        .requestMatchers(HttpMethod.PUT, "/v1/btrs/*/approve").hasAuthority("APPROVER_ROLE")
                        .requestMatchers(HttpMethod.PUT, "/v1/btrs/*/reject").hasAuthority("APPROVER_ROLE")

                        .anyRequest().authenticated()

                )           .formLogin(form -> form
                        .loginPage("/login")
                        .usernameParameter("username")
                        .passwordParameter("password")
                        .successHandler(successHandler)
                        .failureHandler(failureHandler)
                        .permitAll()
                );

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
