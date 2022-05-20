package com.academy.springwebapplication.model;

import lombok.Data;

@Data
public class CreditCard {
    private String number;
    private int monthOfCardExpiration;
    private int yearOfCardExpiration;
}
