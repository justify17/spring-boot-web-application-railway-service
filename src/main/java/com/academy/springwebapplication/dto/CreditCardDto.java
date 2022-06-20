package com.academy.springwebapplication.dto;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
public class CreditCardDto {

    @NotBlank(message = "The field card number must not be empty")
    @Pattern(regexp = "\\b\\d{4}\\s\\d{4}\\s\\d{4}\\s\\d{4}\\b", message = "Invalid card number")
    private String number;

    @Min(value = 1, message = "Invalid card expiration month")
    @Max(value = 12, message = "Invalid card expiration month")
    private int monthOfCardExpiration;

    @Min(value = 22, message = "Invalid card expiration year")
    @Max(value = 99, message = "Invalid card expiration year")
    private int yearOfCardExpiration;
}
