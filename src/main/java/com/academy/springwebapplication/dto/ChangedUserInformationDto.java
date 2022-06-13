package com.academy.springwebapplication.dto;

import lombok.Data;

@Data
public class ChangedUserInformationDto {
    private String username;
    private String firstName;
    private String surname;
    private String phoneNumber;
}
