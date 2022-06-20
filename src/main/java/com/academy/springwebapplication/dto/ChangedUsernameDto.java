package com.academy.springwebapplication.dto;

import com.academy.springwebapplication.annotation.UsernameNotTaken;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class ChangedUsernameDto {
    private String username;

    @NotBlank(message = "The field new username must not be empty")
    @Pattern(regexp = "[0-9a-zA-Z]{4,}",
            message = "The field new username must contain at least 4 characters and contain only numbers or Latin letters")
    @UsernameNotTaken
    private String newUsername;
}
