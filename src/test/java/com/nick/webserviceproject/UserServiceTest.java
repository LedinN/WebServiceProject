package com.nick.webserviceproject;

import com.nick.webserviceproject.authorities.UserRole;
import com.nick.webserviceproject.dto.CustomUserDTO;
import com.nick.webserviceproject.dto.RegistrationRequestDTO;
import com.nick.webserviceproject.model.CustomUser;
import com.nick.webserviceproject.repository.UserRepository;
import com.nick.webserviceproject.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    public void changePassword_UserExists_PasswordUpdated() {
        CustomUser user = new CustomUser(
                "user", "oldPassword", UserRole.USER, true, true, true, true
        );
        user.setId(1L);

        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        Mockito.when(passwordEncoder.encode("newPassword")).thenReturn("encodedPassword");

        userService.changePassword(1L, "newPassword");

        Mockito.verify(userRepository).save(Mockito.argThat(savedUser ->
                savedUser.getPassword().equals("encodedPassword")));
    }

    @Test
    public void changePassword_UserNotFound_ThrowsException() {
        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.empty());

        Assertions.assertThrows(IllegalArgumentException.class, () -> userService.changePassword(1L, "newPassword"));
    }

    @Test
    public void registerUser_Success() {
        CustomUser savedUser = new CustomUser(
                "newUser", "encodedPassword", UserRole.USER, true, true, true, true
        );
        savedUser.setId(1L);

        Mockito.when(userRepository.findByUsername("newUser")).thenReturn(Optional.empty());
        Mockito.when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        Mockito.when(userRepository.save(any(CustomUser.class))).thenReturn(savedUser);

        RegistrationRequestDTO newUser = new RegistrationRequestDTO();
        newUser.setUsername("newUser");
        newUser.setPassword("password");
        newUser.setUserRole(UserRole.USER.toString());
        CustomUser result = userService.registerUser(newUser);

        Assertions.assertEquals("newUser", result.getUsername());
        Assertions.assertEquals("encodedPassword", result.getPassword());
    }

    @Test
    public void registerUser_UsernameAlreadyTaken_ThrowsException() {
        Mockito.when(userRepository.findByUsername("existingUser")).thenReturn(Optional.of(new CustomUser()));

        RegistrationRequestDTO existingUser = new RegistrationRequestDTO();
        existingUser.setUsername("existingUser");
        existingUser.setPassword("password");
        existingUser.setUserRole(UserRole.USER.toString());

        Assertions.assertThrows(IllegalArgumentException.class, () ->
                userService.registerUser(existingUser));
    }

    @Test
    public void promoteToAdmin_Success() {
        CustomUser user = new CustomUser(
                "user", "password", UserRole.USER, true, true, true, true
        );
        user.setId(1L);

        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        userService.promoteToAdmin(1L);

        Mockito.verify(userRepository).save(Mockito.argThat(savedUser ->
                savedUser.getUserRole() == UserRole.ADMIN));
    }

    @Test
    public void promoteToAdmin_UserAlreadyAdmin_ThrowsException() {
        CustomUser adminUser = new CustomUser(
                "admin", "password", UserRole.ADMIN, true, true, true, true
        );
        adminUser.setId(1L);

        Mockito.when(userRepository.findById(1L)).thenReturn(Optional.of(adminUser));

        Assertions.assertThrows(IllegalArgumentException.class, () -> userService.promoteToAdmin(1L));
    }

    @Test
    public void getAllUsers_Success() {
        CustomUser user1 = new CustomUser("user1", "password1", UserRole.USER, true, true, true, true);
        user1.setId(1L);
        CustomUser user2 = new CustomUser("user2", "password2", UserRole.ADMIN, true, true, true, true);
        user2.setId(2L);

        Mockito.when(userRepository.findAll()).thenReturn(List.of(user1, user2));

        List<CustomUserDTO> users = userService.getAllUsers();

        Assertions.assertEquals(2, users.size());
        Assertions.assertEquals("user1", users.get(0).getUsername());
        Assertions.assertEquals("user2", users.get(1).getUsername());
    }

    @Test
    public void deleteUserById_Success() {
        Mockito.when(userRepository.existsById(1L)).thenReturn(true);

        userService.deleteUserById(1L);

        Mockito.verify(userRepository).deleteById(1L);
    }

    @Test
    public void deleteUserById_UserNotFound_ThrowsException() {
        Mockito.when(userRepository.existsById(1L)).thenReturn(false);

        Assertions.assertThrows(IllegalArgumentException.class, () -> userService.deleteUserById(1L));
    }

    @Test
    public void getAuthenticatedUser_Success() {
        CustomUser user = new CustomUser("authenticatedUser", "password", UserRole.USER, true, true, true, true);
        user.setId(1L);

        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken("authenticatedUser", null, user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        Mockito.when(userRepository.findByUsername("authenticatedUser")).thenReturn(Optional.of(user));

        CustomUserDTO result = userService.getAuthenticatedUser();

        Assertions.assertEquals("notauthenticatedUser", result.getUsername());
        Assertions.assertEquals(UserRole.USER.name(), result.getRole());
    }

}
