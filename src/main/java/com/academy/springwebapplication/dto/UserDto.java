package com.academy.springwebapplication.dto;

import lombok.Data;

@Data
public class UserDto {
    private String username;
    private RoleDto role;
    private boolean accountNonLocked;
    private String firstName;
    private String surname;
    private String phoneNumber;
}
