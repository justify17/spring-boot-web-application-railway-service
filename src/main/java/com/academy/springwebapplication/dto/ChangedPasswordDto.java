package com.academy.springwebapplication.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class ChangedPasswordDto {
    private String username;

    @NotBlank(message = "The field old password must not be empty")
    private String password;

    @NotNull
    @Pattern(regexp = "[0-9a-zA-Z]{4,}",
            message = "The field new password must contain at least 4 characters and contain only numbers or Latin letters")
    private String newPassword;

    @NotBlank(message = "The field confirm new password must not be empty")
    private String confirmNewPassword;
}
