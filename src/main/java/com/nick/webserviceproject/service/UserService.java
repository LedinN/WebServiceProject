package com.nick.webserviceproject.service;

import com.nick.webserviceproject.authorities.UserRole;
import com.nick.webserviceproject.dto.CustomUserDTO;
import com.nick.webserviceproject.dto.RegistrationRequestDTO;
import com.nick.webserviceproject.model.CustomUser;
import com.nick.webserviceproject.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<CustomUserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(user -> new CustomUserDTO(user.getId(), user.getUsername(), user.getUserRole().name()))
                .toList();
    }

    public CustomUser registerUser(RegistrationRequestDTO registrationRequest) {
        if (userRepository.findByUsername(registrationRequest.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username is already taken");
        }

        CustomUser newUser = new CustomUser();
        newUser.setUsername(registrationRequest.getUsername());
        newUser.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
        newUser.setUserRole(registrationRequest.getUserRole());

        return userRepository.save(newUser);
    }

    public void deleteUserById(Long id) {
        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("User not found");
        }
        userRepository.deleteById(id);
    }

    public CustomUserDTO getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            throw new IllegalArgumentException("User is not authenticated");
        }

        String username = authentication.getName();
        CustomUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return new CustomUserDTO(user.getId(), user.getUsername(), user.getUserRole().name());
    }

    public void changePassword(Long userId, String newPassword) {
        CustomUser user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    public void promoteToAdmin(Long userId) {
        CustomUser user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (user.getUserRole() == UserRole.ADMIN) {
            throw new IllegalArgumentException("User is already an admin");
        }

        user.setUserRole(UserRole.ADMIN);
        userRepository.save(user);
    }
}

