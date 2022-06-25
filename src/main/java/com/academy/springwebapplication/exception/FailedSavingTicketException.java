package com.academy.springwebapplication.exception;

public class FailedSavingTicketException extends RuntimeException {
    public FailedSavingTicketException(String ticketUsername) {
        super(String.format("Error when saving the ticket of the user \"%s\"", ticketUsername));
    }
}
