package com.nick.webserviceproject.config;

import com.nick.webserviceproject.authorities.UserRole;
import com.nick.webserviceproject.model.CustomUser;
import com.nick.webserviceproject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${adminSecret}")
    private String adminPassword;

    public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void initializeAdminUser() {
        String adminUsername = "SuperAdmin";

        if (userRepository.findByUsername(adminUsername).isEmpty()) {
            CustomUser adminUser = new CustomUser();
            adminUser.setUsername(adminUsername);
            adminUser.setPassword(passwordEncoder.encode(adminPassword));
            adminUser.setUserRole(UserRole.ADMIN);

            userRepository.save(adminUser);
            System.out.println("Admin user created with username: " + adminUsername);
        } else {
            System.out.println("Admin user already exists. Skipping creation.");
        }
    }
}
