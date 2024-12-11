package com.nick.webserviceproject;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nick.webserviceproject.authorities.UserRole;
import com.nick.webserviceproject.dto.RegistrationRequestDTO;
import com.nick.webserviceproject.dto.UpdatePasswordDTO;
import com.nick.webserviceproject.model.CustomUser;
import com.nick.webserviceproject.repository.UserRepository;
import org.hibernate.sql.Update;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setUp(){
        userRepository.deleteAll();

        CustomUser adminUser = new CustomUser();
        adminUser.setUsername("admin");
        adminUser.setPassword(passwordEncoder.encode("adminPassword"));
        adminUser.setUserRole(UserRole.ADMIN);
        userRepository.save(adminUser);

        CustomUser regularUser = new CustomUser();
        regularUser.setUsername("user");
        regularUser.setPassword(passwordEncoder.encode("userPassword"));
        regularUser.setUserRole(UserRole.USER);
        userRepository.save(regularUser);
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void changePassword_AsAdmin() throws Exception {
        Long userId = userRepository.findByUsername("user").get().getId();

        UpdatePasswordDTO updatePasswordDTO = new UpdatePasswordDTO();
        updatePasswordDTO.setNewPassword("newPassword123");

        mockMvc.perform(put("/api/user/change-password/{id}", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(updatePasswordDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Password updated successfully"));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void changePassword_AsUser() throws Exception {

        Long userId = userRepository.findByUsername("user").get().getId();
        UpdatePasswordDTO updatePasswordDTO = new UpdatePasswordDTO();
        updatePasswordDTO.setNewPassword("newPassword123");


        mockMvc.perform(put("/api/user/change-password/{id}", userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(updatePasswordDTO)))
                .andExpect(status().isForbidden());
    }

    @Test
    public void authenticateUser_Success() throws Exception {
        mockMvc.perform(post("/api/user/login")
                .param("username", "admin")
                .param("password", "adminPassword"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isNotEmpty());
    }

    @Test
    public void authenticateUser_BadCredentials() throws Exception {
        mockMvc.perform(post("/api/user/login")
                        .param("username", "admin")
                        .param("password", "wrongPassword"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid username or password"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void whoAmI_Authenticated() throws Exception {
        mockMvc.perform(get("/api/user/who-am-i"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("admin"))
                .andExpect(jsonPath("$.role").value("ADMIN"));
    }

    @Test
    public void whoAmI_Unauthenticated() throws Exception {
        mockMvc.perform(get("/api/user/who-am-i"))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string(""));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void deleteUser_Success() throws Exception {
        Long userId = userRepository.findByUsername("user").get().getId();

        mockMvc.perform(delete("/api/user/delete/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User deleted successfully"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void promoteToAdmin_Success() throws Exception {
        Long userId = userRepository.findByUsername("user").get().getId();

        mockMvc.perform(put("/api/user/promote/{id}", userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User promoted to admin successfully"))
                .andExpect(jsonPath("$.userId").value(userId));
    }

    @Test
    public void registerUser_Success() throws Exception {

        RegistrationRequestDTO registrationRequest = new RegistrationRequestDTO();
        registrationRequest.setUsername("newUser");
        registrationRequest.setPassword("password123");
        registrationRequest.setUserRole(String.valueOf(UserRole.USER));

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

        mockMvc.perform(post("/api/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(registrationRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Username is already taken"));
    }

}
