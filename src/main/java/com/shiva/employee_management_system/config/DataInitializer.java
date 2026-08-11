package com.shiva.employee_management_system.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.shiva.employee_management_system.entity.User;
import com.shiva.employee_management_system.repository.UserRepository;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner createUsers(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder) {

        return args -> {

            // =========================
            // ADMIN USER
            // =========================

            User admin = userRepository.findByUsername("admin")
                    .orElseGet(User::new);

            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole("ADMIN");

            userRepository.save(admin);


            // =========================
            // NORMAL USER
            // =========================

            User user = userRepository.findByUsername("user")
                    .orElseGet(User::new);

            user.setUsername("user");
            user.setPassword(passwordEncoder.encode("user123"));
            user.setRole("USER");

            userRepository.save(user);


            // =========================
            // CONSOLE MESSAGE
            // =========================

            System.out.println("=================================");
            System.out.println("ADMIN username : admin");
            System.out.println("ADMIN password : admin123");
            System.out.println("USER username  : user");
            System.out.println("USER password  : user123");
            System.out.println("Users initialized successfully.");
            System.out.println("=================================");
        };
    }
}