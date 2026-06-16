package com.metrowest.component;

import com.metrowest.entity.Role;
import com.metrowest.entity.User;
import com.metrowest.repo.UserRepository;
import org.jspecify.annotations.NonNull;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserInitializer implements CommandLineRunner
{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserInitializer(UserRepository userRepository)
    {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    @Override
    public void run(String @NonNull ... args)
    {
        if (!userRepository.existsByUsername("admin"))
        {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword_hash(passwordEncoder.encode("admin567"));
            admin.setRole(Role.ADMIN);
            userRepository.save(admin);
        }
    }
}
