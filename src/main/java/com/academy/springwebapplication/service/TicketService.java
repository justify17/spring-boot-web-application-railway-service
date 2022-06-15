package com.academy.springwebapplication.service;

import com.academy.springwebapplication.dto.*;
import com.academy.springwebapplication.model.entity.Departure;
import com.academy.springwebapplication.model.entity.Route;
import com.academy.springwebapplication.model.entity.Ticket;

import java.util.List;

public interface TicketService {
    void payTicket(CreditCard card, TicketDto ticketDto);

    boolean isTicketExists(DepartureDto departureDto, Seat seat);

    List<TicketDto> generateTicketsSuitableForUserRoute(List<Departure> departures, UserRouteDto userRouteDto);

    TicketDto createTicketForDepartureAlongRoute(Departure departure, UserRouteDto userRouteDto);

    void setCarriageComfortLevelForTicket(TicketDto ticketDto);

    void setTicketFinalPrice(TicketDto ticketDto);

    List<TicketDto> getUserTickets(String username);

    void deleteTicketById(Integer ticketId);

    int getNumberOfPurchasedTicketsForDeparture(Integer departureId);

    List<TicketDto> getAllTicketsByDeparture(Integer departureId);
}
