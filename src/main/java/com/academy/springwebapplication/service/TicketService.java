package com.academy.springwebapplication.service;

import com.academy.springwebapplication.dto.TicketDto;

import java.util.List;

public interface TicketService {
    List<TicketDto> getUserTickets(String username);

    void deleteTicketById(Integer ticketId);

    void checkIfTicketIdIsValid(Integer ticketId);

    int getNumberOfPurchasedTicketsForDeparture(Integer departureId);

    List<TicketDto> getAllTicketsByDeparture(Integer departureId);
}
