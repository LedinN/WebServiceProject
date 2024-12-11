package com.nick.webserviceproject;

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
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

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

        Long userId = 1L;
        String newPassword = "newPassword";
        String encodedPassword = "encodedNewPassword";

        CustomUser user = new CustomUser();
        user.setId(userId);
        user.setPassword("oldPassword");

        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        Mockito.when(passwordEncoder.encode(newPassword)).thenReturn(encodedPassword);

        userService.changePassword(userId, newPassword);

        Mockito.verify(passwordEncoder).encode(newPassword);
        Mockito.verify(userRepository).save(Mockito.argThat(savedUser -> savedUser.getPassword().equals(encodedPassword)
        ));
    }

    @Test
    public void changePassword_UserDoesNotExist_PasswordNotUpdated() {
        Long userId = 1L;
        Mockito.when(userRepository.findById(userId)).thenReturn(Optional.empty());

        Assertions.assertThrows(IllegalArgumentException.class, () -> userService.changePassword(userId, "newPassword"));
    }



}


