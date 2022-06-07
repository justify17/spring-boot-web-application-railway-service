package com.academy.springwebapplication.dto;

import lombok.Data;

@Data
public class ChangedAccountDataDto {
    private String username;
    private String newUsername;
    private String password;
    private String newPassword;
    private String confirmNewPassword;
}
