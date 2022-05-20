package com.academy.springwebapplication.service;

import com.academy.springwebapplication.model.entity.Departure;
import com.academy.springwebapplication.model.entity.Route;
import com.academy.springwebapplication.model.entity.Ticket;

import java.util.List;

public interface TicketService {
    void saveTicket(Ticket ticket);

    List<Ticket> getTicketsForDeparturesAlongTheRoute(List<Departure> departures, Route route);
}
