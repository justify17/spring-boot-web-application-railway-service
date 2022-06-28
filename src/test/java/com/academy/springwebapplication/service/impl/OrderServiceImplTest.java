package com.academy.springwebapplication.service.impl;

import com.academy.springwebapplication.dto.CreditCardDto;
import com.academy.springwebapplication.dto.SeatDto;
import com.academy.springwebapplication.dto.TicketDto;
import com.academy.springwebapplication.exception.FailedPaymentException;
import com.academy.springwebapplication.exception.FailedSavingTicketException;
import com.academy.springwebapplication.mapper.TicketMapper;
import com.academy.springwebapplication.model.entity.Station;
import com.academy.springwebapplication.model.entity.Ticket;
import com.academy.springwebapplication.model.entity.User;
import com.academy.springwebapplication.model.repository.StationRepository;
import com.academy.springwebapplication.model.repository.TicketRepository;
import com.academy.springwebapplication.model.repository.UserRepository;
import com.academy.springwebapplication.util.TestObjectFactory;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.powermock.reflect.Whitebox.invokeMethod;

@RunWith(PowerMockRunner.class)
@SpringBootTest
class OrderServiceImplTest {

    @InjectMocks
    private OrderServiceImpl orderService;

    @InjectMocks
    private TestObjectFactory testObjectFactory;

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private StationRepository stationRepository;

    @Mock
    private TicketMapper ticketMapper;

    @Test
    void whenGetSeatsByTicketData() {
        TicketDto ticketDto = testObjectFactory.getTicketDto();
        ticketDto.setSeatNumber(20);
        ticketDto.setCarriageNumber(1);
        ticketDto.setDepartureDate(LocalDateTime.now());
        ticketDto.setArrivalDate(LocalDateTime.now().plusHours(1));

        int numberOfSeats = ticketDto.getDeparture().getTrain().getCarriages().get(0).getNumberOfSeats();

        List<SeatDto> result = new ArrayList<>();
        for (int i = 1; i <= numberOfSeats; i++) {
            SeatDto seat = new SeatDto();
            seat.setNumber(i);
            seat.setCarriageNumber(ticketDto.getCarriageNumber());

            when(ticketRepository.findByTicketData(ticketDto.getDeparture().getId(), ticketDto.getCarriageNumber(),
                    ticketDto.getSeatNumber(), ticketDto.getDepartureDate(), ticketDto.getArrivalDate()))
                    .thenReturn(Collections.emptyList());

            seat.setFree(true);

            result.add(seat);
        }

        assertEquals(result, orderService.getSeatsByTicketData(ticketDto));
        assertNotNull(ticketDto.getSeatNumber());
    }

    @Test
    void whenGetNumberOfSeatsInCarriage() throws Exception {
        TicketDto ticketDto = testObjectFactory.getTicketDto();
        ticketDto.setCarriageNumber(1);

        int result = ticketDto.getDeparture().getTrain().getCarriages().get(0).getNumberOfSeats();

        int actual = invokeMethod(orderService, "getNumberOfSeatsInCarriage", ticketDto);

        assertEquals(result, actual);
    }

    @Test
    void whenIsTicketExisting() throws Exception {
        TicketDto ticketDto = testObjectFactory.getTicketDto();
        ticketDto.setCarriageNumber(1);
        ticketDto.setSeatNumber(1);
        ticketDto.setDepartureDate(LocalDateTime.now());
        ticketDto.setArrivalDate(LocalDateTime.now().plusHours(1));

        List<Ticket> existingTickets = Collections.emptyList();

        when(ticketRepository.findByTicketData(ticketDto.getDeparture().getId(), ticketDto.getCarriageNumber(),
                ticketDto.getSeatNumber(), ticketDto.getDepartureDate(), ticketDto.getArrivalDate()))
                .thenReturn(existingTickets);

        boolean actual = invokeMethod(orderService, "isTicketExisting", ticketDto);

        assertFalse(actual);
    }

    @Test
    void whenSetCarriageComfortLevelForTicket() {
        TicketDto ticketDto = testObjectFactory.getTicketDto();
        ticketDto.setCarriageNumber(1);

        orderService.setCarriageComfortLevelForTicket(ticketDto);

        assertNotNull(ticketDto.getCarriageComfortLevel());
    }

    @Test
    void whenSetTicketFinalPrice_AndRouteTypeIsNotRegional() {
        TicketDto ticketDto = testObjectFactory.getTicketDto();
        ticketDto.setCarriageComfortLevel("LUX");
        ticketDto.getDeparture().getRoute().setType("International");

        orderService.setTicketFinalPrice(ticketDto);

        assertEquals(750, ticketDto.getAdditionalPrice());
        assertEquals(22.5, ticketDto.getFinalPrice());
    }

    @Test
    void whenSaveTicket_AndSuccessfulSave() throws Exception {
        TicketDto ticketDto = testObjectFactory.getTicketDto();
        ticketDto.getDepartureStation().setTitle("Гомель");
        ticketDto.getArrivalStation().setTitle("Минск");

        Ticket ticket = new Ticket();
        when(ticketMapper.ticketDtoToTicket(ticketDto)).thenReturn(ticket);

        User user = new User();
        user.setId(1);

        when(userRepository.findByUsername(ticketDto.getUsername())).thenReturn(user);

        Station departureStation = new Station();
        departureStation.setTitle("Гомель");

        when(stationRepository.findByTitle(ticketDto.getDepartureStation().getTitle())).thenReturn(departureStation);

        Station arrivalStation = new Station();
        arrivalStation.setTitle("Минск");

        when(stationRepository.findByTitle(ticketDto.getArrivalStation().getTitle())).thenReturn(arrivalStation);

        ticket.setId(1);

        invokeMethod(orderService, "saveTicket", ticketDto);

        assertNotNull(ticket.getId());
        assertNotNull(ticket.getUser());
        assertNotNull(ticket.getUserDepartureStation());
        assertNotNull(ticket.getUserArrivalStation());

        verify(ticketMapper, times(1)).ticketDtoToTicket(ticketDto);
        verify(userRepository, times(1)).findByUsername(ticketDto.getUsername());
        verify(stationRepository, times(1)).findByTitle(ticketDto.getDepartureStation().getTitle());
        verify(stationRepository, times(1)).findByTitle(ticketDto.getArrivalStation().getTitle());
        verify(ticketRepository, times(1)).save(ticket);
    }

    @Test
    void whenSaveTicket_AndUnsuccessfulSave() throws Exception {
        TicketDto ticketDto = testObjectFactory.getTicketDto();
        ticketDto.getDepartureStation().setTitle("Гомель");
        ticketDto.getArrivalStation().setTitle("Минск");

        Ticket ticket = new Ticket();
        when(ticketMapper.ticketDtoToTicket(ticketDto)).thenReturn(ticket);

        User user = new User();
        user.setId(1);

        when(userRepository.findByUsername(ticketDto.getUsername())).thenReturn(user);

        Station departureStation = new Station();
        departureStation.setTitle("Гомель");

        when(stationRepository.findByTitle(ticketDto.getDepartureStation().getTitle())).thenReturn(departureStation);

        Station arrivalStation = new Station();
        arrivalStation.setTitle("Минск");

        when(stationRepository.findByTitle(ticketDto.getArrivalStation().getTitle())).thenReturn(arrivalStation);

        ticket.setId(null);

        assertThrows(FailedSavingTicketException.class, () ->
                invokeMethod(orderService, "saveTicket", ticketDto));

        assertNull(ticket.getId());
        assertNotNull(ticket.getUser());
        assertNotNull(ticket.getUserDepartureStation());
        assertNotNull(ticket.getUserArrivalStation());

        verify(ticketMapper, times(1)).ticketDtoToTicket(ticketDto);
        verify(userRepository, times(1)).findByUsername(ticketDto.getUsername());
        verify(stationRepository, times(1)).findByTitle(ticketDto.getDepartureStation().getTitle());
        verify(stationRepository, times(1)).findByTitle(ticketDto.getArrivalStation().getTitle());
        verify(ticketRepository, times(1)).save(ticket);
    }

    @Test
    void whenMoneyWriteOff_AndCardNumberIsEmpty() {
        double price = 12.00;

        CreditCardDto creditCardDto = testObjectFactory.getValidCreditCardDto();
        creditCardDto.setNumber("");

        assertThrows(FailedPaymentException.class, () ->
                invokeMethod(orderService, "moneyWriteOff", creditCardDto, price));
    }
}
