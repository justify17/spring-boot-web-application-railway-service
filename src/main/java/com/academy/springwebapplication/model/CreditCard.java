package com.academy.springwebapplication.model;

import lombok.Data;

@Data
public class CreditCard {
    private String number;
    private int monthOfCardExpiration;
    private int yearOfCardExpiration;

    public void moneyTransfer(){
        if(number.trim().length() < 16){
            throw new RuntimeException("Ticket payment error");
        }

        System.out.println("Payment was successful!");
    }
}
