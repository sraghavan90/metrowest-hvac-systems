package com.metrowest.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig
{
    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http)
    {
        http.authorizeHttpRequests(auth ->
        {
            auth.requestMatchers(
                    "/",
                    "/about",
                    "/signup",
                    "/login")
                .permitAll();

            auth.requestMatchers("/customer/**")
                .hasRole("CUSTOMER");

            auth.requestMatchers("/technician/**")
                .hasRole("TECHNICIAN");

            auth.requestMatchers("/manager/**")
                .hasRole("MANAGER");

            auth.requestMatchers("/admin/**")
                .hasRole("ADMIN");

            auth.anyRequest().authenticated();
        });

        http.formLogin((formLogin) -> formLogin
            .loginPage("/login")
            .usernameParameter("username")
            .passwordParameter("password")
            .successForwardUrl("/role_check")
            .permitAll());

        return http.build();
    }
}
