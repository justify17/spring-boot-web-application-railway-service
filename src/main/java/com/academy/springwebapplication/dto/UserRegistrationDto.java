package com.academy.springwebapplication.dto;

import com.academy.springwebapplication.annotation.UsernameNotTaken;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRegistrationDto {

    @NotBlank(message = "The field username must not be empty")
    @Pattern(regexp = "[0-9a-zA-Z]{4,}",
            message = "The field username must contain at least 4 characters and contain only numbers or Latin letters")
    @UsernameNotTaken
    private String username;

    @NotBlank(message = "The field password must not be empty")
    @Pattern(regexp = "[0-9a-zA-Z]{4,}",
            message = "The field password must contain at least 4 characters and contain only numbers or Latin letters")
    private String password;
}
