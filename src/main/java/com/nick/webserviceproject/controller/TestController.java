package com.nick.webserviceproject.controller;

import com.nick.webserviceproject.authorities.UserRole;
import com.nick.webserviceproject.config.security.CustomUserDetailsService;
import com.nick.webserviceproject.model.CustomUser;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/test")
public class TestController {

    @Value("${adminSecret}")
    private String adminPassword;
    private final CustomUserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    public TestController(CustomUserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/create-admin-user")
    public ResponseEntity<String> createTestUser() {
        CustomUser user = new CustomUser();
        user.setUsername("Admin");
        user.setPassword(passwordEncoder.encode(adminPassword));
        user.setUserRole(UserRole.ADMIN);

        userDetailsService.saveUser(user);
        System.out.println(user.getUserRole());
        return ResponseEntity.ok("Admin user created successfully!");
    }
}

