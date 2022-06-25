package com.academy.springwebapplication.exception;

public class FailedPaymentException extends RuntimeException {
    public FailedPaymentException(String cardNumber) {
        super(String.format("An error occurred while paying by card %s", cardNumber));
    }
}
