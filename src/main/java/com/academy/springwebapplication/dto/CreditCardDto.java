package com.academy.springwebapplication.dto;

import lombok.Data;

@Data
public class CreditCardDto {
    private String number;
    private int monthOfCardExpiration;
    private int yearOfCardExpiration;
}
