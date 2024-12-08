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
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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
    public ResponseEntity<?> authenticateUser(
            @RequestParam String username,
            @RequestParam String password,
            HttpServletResponse response
    ) {

        try {
            System.out.println("INSIDE LOGIN");
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
            System.out.println("AFTER AUTHENTICATION TOKEN: "+authenticationToken);

            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            System.out.println("AFTER AUTHENTICATION TOKEN 2: "+authenticationToken);

            CustomUserDetails customUser = (CustomUserDetails) authentication.getPrincipal();


            List<String> roles = customUser
                    .getAuthorities()
                    .stream()
                    .map(GrantedAuthority::getAuthority).toList();
            System.out.println("CUSTOM USER"+roles);

            final String token = jwtUtils.generateJwtToken(
                    customUser.getUsername(),
                    roles
            );
            System.out.println("JWT TOKEN: "+token);

            Cookie cookie = new Cookie("authToken",token);
            cookie.setHttpOnly(true);
            cookie.setSecure(true);
            cookie.setPath("/");
            cookie.setMaxAge((int) TimeUnit.HOURS.toSeconds(1));
            response.addCookie(cookie);
            System.out.println("RESPONSE COOKIE MAX AGE: "+ cookie.getMaxAge());
            System.out.println("HEADER COOKIE: "+response.getHeader("Set-Cookie"));

            return ResponseEntity.ok(token);


        } catch (BadCredentialsException e) {
            clearAuthToken(response);
            return ResponseEntity.status(401).body("Invalid username or password");
        } catch (Exception e) {
            clearAuthToken(response);
            return ResponseEntity.status(500).body("An error occured while loggin in"+e);
        }

        }

        private void clearAuthToken(HttpServletResponse response) {
            Cookie expiredCookie = new Cookie("authToken", null);
            expiredCookie.setHttpOnly(true);
            expiredCookie.setSecure(true);
            expiredCookie.setPath("/");
            expiredCookie.setMaxAge(0);
            response.addCookie(expiredCookie);
        }

        @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(HttpServletResponse response) {
        Cookie cookie = new Cookie("authToken", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        return ResponseEntity.ok("Logged out successfully");
        }
    }


