package com.academy.springwebapplication.service.impl;

import com.academy.springwebapplication.dto.TicketDto;
import com.academy.springwebapplication.exception.EntityByIdNotFoundException;
import com.academy.springwebapplication.mapper.TicketMapper;
import com.academy.springwebapplication.model.entity.Ticket;
import com.academy.springwebapplication.model.repository.TicketRepository;
import com.academy.springwebapplication.service.OrderService;
import com.academy.springwebapplication.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * This class is responsible for receiving information about tickets and managing them
 */
@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {
    private final TicketRepository ticketRepository;
    private final TicketMapper ticketMapper;
    private final OrderService orderService;

    /**
     * Returns all user tickets from the database
     *
     * @param username username
     * @return user tickets
     */
    @Override
    public List<TicketDto> getUserTickets(String username) {
        List<Ticket> tickets = ticketRepository.findByUser_Username(username);

        return tickets.stream()
                .map(ticketMapper::ticketToTicketDto)
                .peek(orderService::setCarriageComfortLevelForTicket)
                .collect(Collectors.toList());
    }

    /**
     * Deletes the ticket with the given id from the database
     *
     * @param ticketId ticket id
     */
    @Override
    public void deleteTicketById(Integer ticketId) {
        ticketRepository.deleteById(ticketId);
    }

    /**
     * Returns number of purchased tickets for the departure from the database
     *
     * @param departureId departure id
     * @return number of purchased tickets for the departure
     */

    @Override
    public int getNumberOfPurchasedTicketsForDeparture(Integer departureId) {

        return ticketRepository.countAllByDeparture_Id(departureId);
    }

    /**
     * Returns all departure tickets from the database
     *
     * @param departureId departure id
     * @return all departure tickets
     */
    @Override
    public List<TicketDto> getAllTicketsByDeparture(Integer departureId) {
        List<Ticket> tickets = ticketRepository.findByDeparture_Id(departureId);

        return tickets.stream()
                .map(ticketMapper::ticketToTicketDto)
                .peek(orderService::setCarriageComfortLevelForTicket)
                .collect(Collectors.toList());
    }

    /**
     * Checks for the presence of the ticket with the given id in the database
     *
     * @param ticketId ticket id
     */
    @Override
    public void checkIfTicketIdIsValid(Integer ticketId) {
        if (!ticketRepository.existsById(ticketId)) {

            throw new EntityByIdNotFoundException(ticketId);
        }
    }
}
