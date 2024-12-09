package com.nick.webserviceproject.controller;

import com.nick.webserviceproject.authorities.UserRole;
import com.nick.webserviceproject.config.security.CustomUserDetailsService;
import com.nick.webserviceproject.model.CustomUser;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/test")
public class TestController {
    private final CustomUserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    public TestController(CustomUserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/create-test-user")
    public ResponseEntity<String> createTestUser() {
        CustomUser user = new CustomUser();
        user.setUsername("testuser4");
        user.setPassword(passwordEncoder.encode("password123"));
        user.setUserRole(UserRole.USER);

        userDetailsService.saveUser(user);
        System.out.println(user.getUserRole());
        return ResponseEntity.ok("Test user created successfully!");
    }
}

