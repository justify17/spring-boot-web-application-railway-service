package com.academy.springwebapplication.service;

import com.academy.springwebapplication.dto.CreditCardDto;
import com.academy.springwebapplication.dto.SeatDto;
import com.academy.springwebapplication.dto.TicketDto;

import java.util.List;

public interface OrderService {
    void payTicket(CreditCardDto card, TicketDto ticketDto);

    void setCarriageComfortLevelForTicket(TicketDto ticketDto);

    void setTicketFinalPrice(TicketDto ticketDto);

    List<SeatDto> getSeatsByTicketData(TicketDto ticketDto);
}
