package com.academy.springwebapplication.dto;

import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class ChangedUserInformationDto {
    private String username;

    @NotNull(message = "The field first name cannot be null")
    @Pattern(regexp = "()|([a-zA-Zа-яА-ЯёЁ-]+)", message = "Incorrect first name")
    private String firstName;

    @NotNull(message = "The field surname cannot be null")
    @Pattern(regexp = "()|([a-zA-Zа-яА-ЯёЁ-]+)", message = "Incorrect surname")
    private String surname;

    @NotNull(message = "The field phone number cannot be null")
    @Pattern(regexp = "()|(\\+375\\(__\\)___-__-__)|(\\+375\\s?[(]?[0-9]{2}[)]?\\s?\\d{3}[-]?\\d{2}[-]?\\d{2})",
            message = "Incorrect phone number")
    private String phoneNumber;
}
