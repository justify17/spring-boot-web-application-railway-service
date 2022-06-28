package com.academy.springwebapplication.service.impl;

import com.academy.springwebapplication.dto.TicketDto;
import com.academy.springwebapplication.exception.EntityByIdNotFoundException;
import com.academy.springwebapplication.mapper.TicketMapper;
import com.academy.springwebapplication.model.entity.Ticket;
import com.academy.springwebapplication.model.repository.TicketRepository;
import com.academy.springwebapplication.service.OrderService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@SpringBootTest
class TicketServiceImplTest {

    @InjectMocks
    private TicketServiceImpl ticketService;

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private TicketMapper ticketMapper;

    @Mock
    private OrderService orderService;

    @Test
    void whenGetUserTickets() {
        String username = "user";

        Ticket ticket = new Ticket();
        ticket.setId(1);

        List<Ticket> tickets = List.of(ticket);

        when(ticketRepository.findByUser_Username(username)).thenReturn(tickets);

        TicketDto ticketDto = new TicketDto();
        ticketDto.setId(1);

        when(ticketMapper.ticketToTicketDto(ticket)).thenReturn(ticketDto);

        List<TicketDto> result = List.of(ticketDto);

        assertEquals(result, ticketService.getUserTickets(username));

        verify(ticketRepository, times(1)).findByUser_Username(username);
        verify(ticketMapper, times(1)).ticketToTicketDto(ticket);
        verify(orderService, times(1)).setCarriageComfortLevelForTicket(ticketDto);
    }

    @Test
    void whenDeleteTicketById() {
        Integer ticketId = 1;

        ticketService.deleteTicketById(ticketId);

        verify(ticketRepository, times(1)).deleteById(ticketId);
    }

    @Test
    void whenGetNumberOfPurchasedTicketsForDeparture() {
        Integer departureId = 1;

        int result = 10;

        when(ticketRepository.countAllByDeparture_Id(departureId)).thenReturn(result);

        assertEquals(result, ticketService.getNumberOfPurchasedTicketsForDeparture(departureId));

        verify(ticketRepository, times(1)).countAllByDeparture_Id(departureId);
    }

    @Test
    void whenCheckIfTicketIdIsValid_AndTicketIsNotExisting() {
        Integer ticketId = 0;

        when(ticketRepository.existsById(ticketId)).thenReturn(false);

        assertThrows(EntityByIdNotFoundException.class, () -> ticketService.checkIfTicketIdIsValid(ticketId));

        verify(ticketRepository, times(1)).existsById(ticketId);
    }

    @Test
    void whenCheckIfTicketIdIsValid_AndTicketIsExisting() {
        Integer ticketId = 1;

        when(ticketRepository.existsById(ticketId)).thenReturn(true);

        ticketService.checkIfTicketIdIsValid(ticketId);

        verify(ticketRepository, times(1)).existsById(ticketId);
    }
}
