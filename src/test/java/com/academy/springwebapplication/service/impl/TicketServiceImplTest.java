package com.academy.springwebapplication.service.impl;

import com.academy.springwebapplication.dto.CreditCardDto;
import com.academy.springwebapplication.dto.RouteStationDto;
import com.academy.springwebapplication.dto.TicketDto;
import com.academy.springwebapplication.dto.UserRouteDto;
import com.academy.springwebapplication.exception.EntityByIdNotFoundException;
import com.academy.springwebapplication.exception.FailedPaymentException;
import com.academy.springwebapplication.exception.FailedSavingTicketException;
import com.academy.springwebapplication.mapper.TicketMapper;
import com.academy.springwebapplication.model.entity.*;
import com.academy.springwebapplication.model.repository.RouteStationRepository;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.powermock.reflect.Whitebox.invokeMethod;

@RunWith(PowerMockRunner.class)
@SpringBootTest
class TicketServiceImplTest {

    @InjectMocks
    private TicketServiceImpl ticketService;

    @InjectMocks
    private TestObjectFactory testObjectFactory;

    @Mock
    private TicketRepository ticketRepository;

    @Mock
    private RouteStationRepository routeStationRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private StationRepository stationRepository;

    @Mock
    private TicketMapper ticketMapper;

    @Test
    void whenMoneyWriteOff_AndCardNumberIsEmpty() {
        double price = 12.00;

        CreditCardDto creditCardDto = testObjectFactory.getValidCreditCardDto();
        creditCardDto.setNumber("");

        assertThrows(FailedPaymentException.class, () ->
                invokeMethod(ticketService, "moneyWriteOff", creditCardDto, price));
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

        invokeMethod(ticketService, "saveTicket", ticketDto);

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
                invokeMethod(ticketService, "saveTicket", ticketDto));

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
    void whenIsTicketSuitableForUserRoute_AndUserDepartureDateAndTimeAreNull() throws Exception {
        TicketDto ticketDto = testObjectFactory.getTicketDto();
        ticketDto.setDepartureDate(LocalDateTime.now().plusDays(5));

        UserRouteDto userRouteDto = testObjectFactory.getValidUserRouteDto();
        userRouteDto.setDepartureDate(null);
        userRouteDto.setDepartureTime(null);

        boolean result = invokeMethod(ticketService,
                "isTicketSuitableForUserRoute", ticketDto, userRouteDto);

        assertTrue(result);
    }

    @Test
    void whenIsTicketDepartureDateEqualUserDepartureDate_AndUserDepartureDateAndTimeAreNotNull() throws Exception {
        TicketDto ticketDto = testObjectFactory.getTicketDto();
        ticketDto.setDepartureDate(LocalDateTime.of(LocalDate.now(), LocalTime.of(14, 0)));

        UserRouteDto userRouteDto = testObjectFactory.getValidUserRouteDto();
        userRouteDto.setDepartureDate(LocalDate.now());
        userRouteDto.setDepartureTime(LocalTime.of(14, 30));

        boolean result = invokeMethod(ticketService,
                "isTicketDepartureDateEqualUserDepartureDate", ticketDto, userRouteDto);

        assertFalse(result);
    }

    @Test
    void whenIsTicketDepartureDateEqualUserDepartureDate_AndUserDepartureTimeIsNull() throws Exception {
        TicketDto ticketDto = testObjectFactory.getTicketDto();
        ticketDto.setDepartureDate(LocalDateTime.of(LocalDate.now(), LocalTime.of(14, 0)));

        UserRouteDto userRouteDto = testObjectFactory.getValidUserRouteDto();
        userRouteDto.setDepartureDate(LocalDate.now());
        userRouteDto.setDepartureTime(null);

        boolean result = invokeMethod(ticketService,
                "isTicketDepartureDateEqualUserDepartureDate", ticketDto, userRouteDto);

        assertTrue(result);
    }

    @Test
    void whenIsTicketDepartureDateEqualUserDepartureDate_AndUserDepartureDateIsNull() throws Exception {
        TicketDto ticketDto = testObjectFactory.getTicketDto();
        ticketDto.setDepartureDate(LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(14, 0)));

        UserRouteDto userRouteDto = testObjectFactory.getValidUserRouteDto();
        userRouteDto.setDepartureDate(null);
        userRouteDto.setDepartureTime(LocalTime.of(14, 30));

        boolean actual = invokeMethod(ticketService,
                "isTicketDepartureDateEqualUserDepartureDate", ticketDto, userRouteDto);

        assertFalse(actual);
    }

    @Test
    void whenSetTicketDepartureAndArrivalDates() throws Exception {
        TicketDto ticketDto = testObjectFactory.getTicketDto();
        ticketDto.getDepartureStation().setTitle("Гомель");
        ticketDto.getArrivalStation().setTitle("Минск");
        ticketDto.setDepartureDate(null);
        ticketDto.setArrivalDate(null);

        RouteStationDto routeStationDto1 = testObjectFactory.getRouteStationDto();
        routeStationDto1.getStation().setTitle("Гомель");
        routeStationDto1.setDepartureDate(LocalDateTime.of(LocalDate.now(), LocalTime.of(14, 0)));

        RouteStationDto routeStationDto2 = testObjectFactory.getRouteStationDto();
        routeStationDto2.getStation().setTitle("Уза");
        routeStationDto2.setArrivalDate(LocalDateTime.of(LocalDate.now(), LocalTime.of(14, 25)));
        routeStationDto2.setDepartureDate(LocalDateTime.of(LocalDate.now(), LocalTime.of(14, 30)));

        RouteStationDto routeStationDto3 = testObjectFactory.getRouteStationDto();
        routeStationDto3.getStation().setTitle("Минск");
        routeStationDto3.setArrivalDate(LocalDateTime.of(LocalDate.now(), LocalTime.of(15, 30)));

        List<RouteStationDto> routeStationDto = List.of(routeStationDto1, routeStationDto2, routeStationDto3);

        ticketDto.getDeparture().getRoute().setRouteStations(routeStationDto);

        invokeMethod(ticketService, "setTicketDepartureAndArrivalDates", ticketDto);

        assertEquals(routeStationDto1.getDepartureDate(), ticketDto.getDepartureDate());
        assertEquals(routeStationDto3.getArrivalDate(), ticketDto.getArrivalDate());
    }

    @Test
    void whenSetTicketRoutePricePerDeparture() throws Exception {
        TicketDto ticketDto = testObjectFactory.getTicketDto();
        ticketDto.getDepartureStation().setTitle("Гомель");
        ticketDto.getArrivalStation().setTitle("Минск");

        RouteStation routeStation1 = new RouteStation();
        routeStation1.setRouteStopNumber(1);
        routeStation1.setPriceToNextStation(100);

        RouteStation routeStation2 = new RouteStation();
        routeStation2.setRouteStopNumber(2);
        routeStation2.setPriceToNextStation(200);

        RouteStation routeStation3 = new RouteStation();
        routeStation3.setRouteStopNumber(3);
        routeStation3.setPriceToNextStation(300);

        RouteStation routeStation4 = new RouteStation();
        routeStation4.setRouteStopNumber(4);
        routeStation4.setPriceToNextStation(400);

        RouteStation routeStation5 = new RouteStation();
        routeStation5.setRouteStopNumber(5);
        routeStation5.setPriceToNextStation(500);

        List<RouteStation> routeStations = List.of(routeStation1, routeStation2, routeStation3, routeStation4, routeStation5);

        Route route = new Route();
        route.setId(1);
        route.setRouteStations(routeStations);

        Departure departure = new Departure();
        departure.setRoute(route);

        RouteStation departureRouteStation = new RouteStation();
        departureRouteStation.setRouteStopNumber(2);

        RouteStation arrivalRouteStation = new RouteStation();
        arrivalRouteStation.setRouteStopNumber(4);

        when(routeStationRepository.findByRoute_IdAndStation_Title(departure.getRoute().getId(),
                ticketDto.getDepartureStation().getTitle())).thenReturn(departureRouteStation);

        when(routeStationRepository.findByRoute_IdAndStation_Title(departure.getRoute().getId(),
                ticketDto.getArrivalStation().getTitle())).thenReturn(arrivalRouteStation);

        invokeMethod(ticketService, "setTicketRoutePricePerDeparture", ticketDto, departure);

        int result = routeStation2.getPriceToNextStation() + routeStation3.getPriceToNextStation();

        assertEquals(result, ticketDto.getRoutePrice());
    }

    @Test
    void whenGetNumberOfSeatsInCarriage() throws Exception {
        TicketDto ticketDto = testObjectFactory.getTicketDto();
        ticketDto.setCarriageNumber(1);

        int result = ticketDto.getDeparture().getTrain().getCarriages().get(0).getNumberOfSeats();

        int actual = invokeMethod(ticketService, "getNumberOfSeatsInCarriage", ticketDto);

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

        boolean actual = invokeMethod(ticketService, "isTicketExisting", ticketDto);

        assertFalse(actual);
    }

    @Test
    void whenSetCarriageComfortLevelForTicket() {
        TicketDto ticketDto = testObjectFactory.getTicketDto();
        ticketDto.setCarriageNumber(1);

        ticketService.setCarriageComfortLevelForTicket(ticketDto);

        assertNotNull(ticketDto.getCarriageComfortLevel());
    }

    @Test
    void whenSetTicketFinalPrice_AndRouteTypeIsNotRegional() {
        TicketDto ticketDto = testObjectFactory.getTicketDto();
        ticketDto.setCarriageComfortLevel("LUX");
        ticketDto.getDeparture().getRoute().setType("International");

        ticketService.setTicketFinalPrice(ticketDto);

        assertEquals(750, ticketDto.getAdditionalPrice());
        assertEquals(22.5, ticketDto.getFinalPrice());
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
