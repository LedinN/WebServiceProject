package com.nick.webserviceproject.controller;

import com.nick.webserviceproject.authorities.UserRole;
import com.nick.webserviceproject.config.security.CustomUserDetails;
import com.nick.webserviceproject.config.security.jwt.JwtUtils;
import com.nick.webserviceproject.dto.CustomUserDTO;
import com.nick.webserviceproject.dto.RegistrationRequestDTO;
import com.nick.webserviceproject.dto.UpdatePasswordDTO;
import com.nick.webserviceproject.model.CustomUser;
import com.nick.webserviceproject.repository.UserRepository;
import com.nick.webserviceproject.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/user")
public class UserController {


    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    @Autowired
    public UserController(UserService userService, UserRepository userRepository, PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, JwtUtils jwtUtils) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegistrationRequestDTO registrationRequest) {
        try {
            userService.registerUser(registrationRequest);
            return ResponseEntity.status(201).body(Map.of("message", "User registered successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(
            @RequestParam String username,
            @RequestParam String password
    ) {
        try {
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username,password));
            return ResponseEntity.ok().body(jwtUtils.generateJwtToken(auth));
        } catch (BadCredentialsException e) {
            System.out.println("Bad credentials: " + e.getMessage());
        }
        return ResponseEntity.badRequest().body("Invalid username or password");
    }

    @GetMapping("/who-am-i")
    public ResponseEntity<CustomUserDTO> whoAmI() {
        try {
            CustomUserDTO userDTO = userService.getAuthenticatedUser();
            return ResponseEntity.ok(userDTO);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(401).body(null);
        }
    }

    @GetMapping("/fetchallusers")
    public ResponseEntity<List<CustomUserDTO>> fetchAllUsers() {
        List<CustomUserDTO> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUserById(id);
            return ResponseEntity.ok(Map.of("message", "User deleted successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(404).body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/change-password/{id}")
    public ResponseEntity<?> changePassword(
            @PathVariable Long id,
            @Valid @RequestBody UpdatePasswordDTO updatePasswordDTO
    ) {
        try {
            userService.changePassword(id, updatePasswordDTO.getNewPassword());
            return ResponseEntity.ok(Map.of("message", "Password updated successfully"));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/promote/{id}")
    public ResponseEntity<?> promoteToAdmin(@PathVariable Long id) {
        try {
            userService.promoteToAdmin(id);
            return ResponseEntity.ok(Map.of("message", "User promoted to admin successfully", "userId", id));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(400).body(Map.of("error", e.getMessage()));
        }
    }

        @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(HttpServletResponse response) {

        return ResponseEntity.ok(Map.of("message", "Logged out successfully"));
        }
    }


