package com.metrowest.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;


@Configuration
public class ApplicationConfig
{
    @Bean
    public UserDetailsService userDetailsService()
    {
        UserDetails admin = User.withDefaultPasswordEncoder()
            .username("admin")
            .password("admin123")
            .roles("ADMIN")
            .build();

        UserDetails manager = User.withDefaultPasswordEncoder()
            .username("manager")
            .password("manager123")
            .roles("MANAGER")
            .build();

        UserDetails technician = User.withDefaultPasswordEncoder()
            .username("tech")
            .password("tech123")
            .roles("TECHNICIAN")
            .build();

        UserDetails user = User.withDefaultPasswordEncoder()
            .username("user")
            .password("user123")
            .roles("CUSTOMER")
            .build();

        return new InMemoryUserDetailsManager(admin, manager, technician, user);
    }
}
