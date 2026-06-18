package com.metrowest.component;

import com.metrowest.entity.Role;
import com.metrowest.entity.User;
import com.metrowest.repo.UserRepository;
import com.metrowest.util.PasswordGenerator;
import org.jspecify.annotations.NonNull;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AppInitializer implements CommandLineRunner
{
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AppInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder)
    {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String @NonNull ... args)
    {
        if (!userRepository.existsByUsername("admin"))
        {
            User admin = new User();
            String password = PasswordGenerator.generateRandomPassword(20);
            admin.setEmail("admin@metrowesthvac.com");
            admin.setUsername("admin");
            admin.setPassword_hash(passwordEncoder.encode(password));
            admin.setRole(Role.ADMIN);
            userRepository.save(admin);
            IO.println("========================================================");
            IO.println("Generated Admin user with password: " + password);
            IO.println("========================================================");
        }
    }
}
