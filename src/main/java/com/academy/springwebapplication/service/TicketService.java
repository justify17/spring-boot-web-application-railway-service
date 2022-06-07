package com.academy.springwebapplication.service;

import com.academy.springwebapplication.dto.*;
import com.academy.springwebapplication.model.entity.Departure;
import com.academy.springwebapplication.model.entity.Route;
import com.academy.springwebapplication.model.entity.Ticket;

import java.util.List;

public interface TicketService {
    void payTicket(CreditCard card, TicketDto ticketDto);

    boolean isTicketExists(DepartureDto departureDto, Seat seat);

    List<TicketDto> getTicketsForDeparturesAlongTheRoute(List<Departure> departures, UserRouteDto userRouteDto);

    TicketDto getTicketForDepartureAlongTheRoute(Departure departure, UserRouteDto userRouteDto);

    void setCarriageComfortLevelForTicket(TicketDto ticketDto);

    void setTicketFinalPrice(TicketDto ticketDto);
}
