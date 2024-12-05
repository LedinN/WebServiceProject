package com.nick.webserviceproject.controller;

import com.nick.webserviceproject.authorities.UserRole;
import com.nick.webserviceproject.config.security.CustomUserDetails;
import com.nick.webserviceproject.config.security.jwt.JwtUtils;
import com.nick.webserviceproject.dto.RegistrationRequestDTO;
import com.nick.webserviceproject.model.CustomUser;
import com.nick.webserviceproject.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    @Autowired
    public UserController(UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    // TODO - NOT NEEDED?
    @GetMapping("/register")
    public String registerUser(Model model) { // Correct Model?
        model.addAttribute("customUser", new CustomUser());
        model.addAttribute("userRoles", UserRole.values());

        return "/register";
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegistrationRequestDTO registrationRequest) {

        if (userRepository.findByUsername(registrationRequest.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("Username is already taken");
        }

        CustomUser newUser = new CustomUser();
        newUser.setUsername(registrationRequest.getUsername());
        newUser.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
        newUser.setUserRole(registrationRequest.getUserRole());

        userRepository.save(newUser);

        return ResponseEntity.status(201).body("User registered successfully");

    }

    @PostMapping("/login")
    public ResponseEntity<String> authenticateUser(
            @RequestParam String username,
            @RequestParam String password,
            HttpServletResponse response
    ) {
        try {
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
            Authentication authentication = authenticationManager.authenticate(authenticationToken);

            CustomUserDetails customUser = (CustomUserDetails) authentication.getPrincipal();

            // if (customUser instanceof CustomUserDetails customUserDetails) {
                final String token = jwtUtils.generateJwtToken(
                        customUser.getUsername(),
                        customUser.getAuthorities().toString()
                );

                Cookie cookie = new Cookie("authToken", token);
                cookie.setHttpOnly(true);
                cookie.setSecure(true);
                cookie.setPath("/");
                cookie.setMaxAge((int) TimeUnit.HOURS.toSeconds(1));
                response.addCookie(cookie);

                return ResponseEntity.ok(token);
            } catch (BadCredentialsException e) {
            return ResponseEntity.status(401).body("Bad credentials");
        //else {
            //return ResponseEntity.internalServerError().body("principal not of type CustomUserDetails");}
        }

        }
    }


