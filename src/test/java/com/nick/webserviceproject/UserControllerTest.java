package com.nick.webserviceproject;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nick.webserviceproject.dto.CustomUserDTO;
import com.nick.webserviceproject.dto.RegistrationRequestDTO;
import com.nick.webserviceproject.dto.UpdatePasswordDTO;
import com.nick.webserviceproject.model.CustomUser;
import com.nick.webserviceproject.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;


import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    private CustomUserDTO adminUser;
    private CustomUserDTO regularUser;

    @BeforeEach
    public void setUp() {
        adminUser = new CustomUserDTO(1L, "admin", "ADMIN");
        regularUser = new CustomUserDTO(2L, "user", "USER");
    }

    @Test
    public void registerUser_Success() throws Exception {
        RegistrationRequestDTO registrationRequest = new RegistrationRequestDTO();
        registrationRequest.setUsername("newUser");
        registrationRequest.setPassword("password123");
        registrationRequest.setUserRole("USER");

        Mockito.when(userService.registerUser(any(RegistrationRequestDTO.class))).thenReturn(null);

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
        registrationRequest.setUserRole("USER");

        Mockito.doThrow(new IllegalArgumentException("Username is already taken"))
                .when(userService).registerUser(any(RegistrationRequestDTO.class));

        mockMvc.perform(post("/api/user/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(registrationRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Username is already taken"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void changePassword_AsAdmin() throws Exception {
        UpdatePasswordDTO updatePasswordDTO = new UpdatePasswordDTO();
        updatePasswordDTO.setNewPassword("newPassword123");

        Mockito.doNothing().when(userService).changePassword(eq(regularUser.getId()), eq("newPassword123"));

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
        Mockito.doNothing().when(userService).promoteToAdmin(eq(regularUser.getId()));

        mockMvc.perform(put("/api/user/promote/{id}", regularUser.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User promoted to admin successfully"))
                .andExpect(jsonPath("$.userId").value(regularUser.getId()));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void deleteUser_Success() throws Exception {
        Mockito.doNothing().when(userService).deleteUserById(eq(regularUser.getId()));

        mockMvc.perform(delete("/api/user/delete/{id}", regularUser.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("User deleted successfully"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    public void fetchAllUsers_Success() throws Exception {
        Mockito.when(userService.getAllUsers()).thenReturn(List.of(adminUser, regularUser));

        mockMvc.perform(get("/api/user/fetchallusers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].username").value("admin"))
                .andExpect(jsonPath("$[1].username").value("user"));
    }
}

