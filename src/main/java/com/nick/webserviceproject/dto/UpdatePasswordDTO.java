package com.nick.webserviceproject.dto;

import jakarta.validation.constraints.NotBlank;

public class UpdatePasswordDTO {
    @NotBlank(message = "New password is required")
    private String newPassword;


    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}
