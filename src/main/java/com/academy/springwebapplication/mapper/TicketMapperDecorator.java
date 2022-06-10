package com.academy.springwebapplication.mapper;

import com.academy.springwebapplication.dto.TicketDto;
import com.academy.springwebapplication.model.entity.Ticket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public abstract class TicketMapperDecorator implements TicketMapper {
    @Autowired
    @Qualifier("delegate")
    private TicketMapper delegate;


    @Override
    public Ticket ticketDtoToTicket(TicketDto ticketDto) {
        Ticket ticket = delegate.ticketDtoToTicket(ticketDto);
        ticket.setPrice((int) (ticketDto.getFinalPrice() * 100));

        return ticket;
    }

    @Override
    public TicketDto ticketToTicketDto(Ticket ticket) {
        TicketDto ticketDto = delegate.ticketToTicketDto(ticket);
        ticketDto.setFinalPrice(ticket.getPrice() / 100.0);

        return ticketDto;
    }

}
