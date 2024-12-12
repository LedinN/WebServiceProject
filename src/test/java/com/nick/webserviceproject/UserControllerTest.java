package com.nick.webserviceproject;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nick.webserviceproject.authorities.UserRole;
import com.nick.webserviceproject.dto.RegistrationRequestDTO;
import com.nick.webserviceproject.dto.UpdatePasswordDTO;
import com.nick.webserviceproject.model.CustomUser;
import com.nick.webserviceproject.repository.UserRepository;
import com.nick.webserviceproject.config.security.jwt.JwtUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private JwtUtils jwtUtils;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private CustomUser adminUser;
    private CustomUser regularUser;

    @BeforeEach
    public void setUp() {
        adminUser = new CustomUser();
        adminUser.setId(1L);
        adminUser.setUsername("admin");
        adminUser.setPassword(passwordEncoder.encode("adminPassword"));
        adminUser.setUserRole(UserRole.ADMIN);

        regularUser = new CustomUser();
        regularUser.setId(2L);
        regularUser.setUsername("user");
        regularUser.setPassword(passwordEncoder.encode("userPassword"));
        regularUser.setUserRole(UserRole.USER);

        Mockito.when(userRepository.findByUsername("admin")).thenReturn(Optional.of(adminUser));
        Mockito.when(userRepository.findByUsername("user")).thenReturn(Optional.of(regularUser));
    }

    @Test
    public void authenticateUser_Success() throws Exception {
        String token = "mockJwtToken";

        Mockito.when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(new UsernamePasswordAuthenticationToken("admin", "adminPassword"));
        Mockito.when(jwtUtils.generateJwtToken(any())).thenReturn(token);

        mockMvc.perform(post("/api/user/login")
                        .param("username", "admin")
                        .param("password", "adminPassword"))
                .andExpect(status().isOk())
                .andExpect(content().string(token));
    }

    @Test
    public void authenticateUser_BadCredentials() throws Exception {
        Mockito.when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        mockMvc.perform(post("/api/user/login")
                        .param("username", "admin")
                        .param("password", "wrongPassword"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid username or password"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void changePassword_AsAdmin() throws Exception {
        UpdatePasswordDTO updatePasswordDTO = new UpdatePasswordDTO();
        updatePasswordDTO.setNewPassword("newPassword123");

        Mockito.when(userRepository.findById(regularUser.getId())).thenReturn(Optional.of(regularUser));

        mockMvc.perform(put("/api/user/change-password/{id}", regularUser.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updatePasswordDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Password updated successfully"));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void changePassword_AsUser() throws Exception {
        UpdatePasswordDTO updatePasswordDTO = new UpdatePasswordDTO();
        updatePasswordDTO.setNewPassword("newPassword123");

        mockMvc.perform(put("/api/user/change-password/{id}", regularUser.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updatePasswordDTO)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void promoteToAdmin_Success() throws Exception {
        Mockito.when(userRepository.findById(regularUser.getId())).thenReturn(Optional.of(regularUser));

        mockMvc.perform(put("/api/user/promote/{id}", regularUser.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User promoted to admin successfully"))
                .andExpect(jsonPath("$.userId").value(regularUser.getId()));
    }

    @Test
    public void registerUser_Success() throws Exception {
        RegistrationRequestDTO registrationRequest = new RegistrationRequestDTO();
        registrationRequest.setUsername("newUser");
        registrationRequest.setPassword("password123");
        registrationRequest.setUserRole(String.valueOf(UserRole.USER));

        Mockito.when(userRepository.findByUsername("newUser")).thenReturn(Optional.empty());

        mockMvc.perform(post("/api/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(registrationRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("User registered successfully"));
    }

    @Test
    public void registerUser_UsernameAlreadyTaken() throws Exception {
        RegistrationRequestDTO registrationRequest = new RegistrationRequestDTO();
        registrationRequest.setUsername("user");
        registrationRequest.setPassword("password123");
        registrationRequest.setUserRole(String.valueOf(UserRole.USER));

        Mockito.when(userRepository.findByUsername("user")).thenReturn(Optional.of(regularUser));

        mockMvc.perform(post("/api/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(registrationRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Username is already taken"));
    }
}
