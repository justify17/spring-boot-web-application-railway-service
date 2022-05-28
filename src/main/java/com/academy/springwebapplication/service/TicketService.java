package com.academy.springwebapplication.service;

import com.academy.springwebapplication.dto.CreditCard;
import com.academy.springwebapplication.model.entity.Departure;
import com.academy.springwebapplication.model.entity.Route;
import com.academy.springwebapplication.model.entity.Ticket;

import java.util.List;

public interface TicketService {
    void payTicket(CreditCard card, Ticket ticket);

    List<Ticket> getTicketsForDeparturesAlongTheRoute(List<Departure> departures, Route route);

    List<Ticket> getPurchasedAndNotPurchasedDepartureTicketsForCarriage(Departure departure, int carriageNumber);

    void setCarriageComfortLevelForTicket(Ticket ticket);

    void setAdditionalTicketPriceForComfortLevelOfCarriage(Ticket ticket);
}
