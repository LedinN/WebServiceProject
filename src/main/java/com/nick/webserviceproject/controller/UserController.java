package com.nick.webserviceproject.controller;

import com.nick.webserviceproject.authorities.UserRole;
import com.nick.webserviceproject.dto.RegistrationRequestDTO;
import com.nick.webserviceproject.model.CustomUser;
import com.nick.webserviceproject.repository.UserRepository;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = "https://localhost:3000", allowedHeaders = "*", allowCredentials = "true")
public class UserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("/register")
    public String registerUser(Model model) { // Correct Model?
        model.addAttribute("customUser", new CustomUser());
        model.addAttribute("userRoles", UserRole.values());

        return "/register";
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegistrationRequestDTO registrationRequest) {

        if (userRepository.findByUsername(registrationRequest.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("Username is already taken");
        }

        CustomUser newUser = new CustomUser();
        newUser.setUsername(registrationRequest.getUsername());
        newUser.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
        newUser.setUserRole(UserRole.USER);

        userRepository.save(newUser);

        return ResponseEntity.status(201).body("User registered successfully");

    }

}
