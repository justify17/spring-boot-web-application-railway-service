package com.academy.springwebapplication.service.impl;

import com.academy.springwebapplication.dto.RouteStationDto;
import com.academy.springwebapplication.dto.TicketDto;
import com.academy.springwebapplication.dto.UserRouteDto;
import com.academy.springwebapplication.model.entity.Departure;
import com.academy.springwebapplication.model.entity.Route;
import com.academy.springwebapplication.model.entity.RouteStation;
import com.academy.springwebapplication.model.repository.RouteStationRepository;
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
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.powermock.reflect.Whitebox.invokeMethod;

@RunWith(PowerMockRunner.class)
@SpringBootTest
class TicketGenerationServiceImplTest {

    @InjectMocks
    private TicketGenerationServiceImpl ticketGenerationService;

    @InjectMocks
    private TestObjectFactory testObjectFactory;

    @Mock
    private RouteStationRepository routeStationRepository;

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

        invokeMethod(ticketGenerationService, "setTicketDepartureAndArrivalDates", ticketDto);

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

        invokeMethod(ticketGenerationService, "setTicketRoutePricePerDeparture", ticketDto, departure);

        int result = routeStation2.getPriceToNextStation() + routeStation3.getPriceToNextStation();

        assertEquals(result, ticketDto.getRoutePrice());
    }

    @Test
    void whenIsTicketSuitableForUserRoute_AndUserDepartureDateAndTimeAreNull() throws Exception {
        TicketDto ticketDto = testObjectFactory.getTicketDto();
        ticketDto.setDepartureDate(LocalDateTime.now().plusDays(5));

        UserRouteDto userRouteDto = testObjectFactory.getValidUserRouteDto();
        userRouteDto.setDepartureDate(null);
        userRouteDto.setDepartureTime(null);

        boolean result = invokeMethod(ticketGenerationService,
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

        boolean result = invokeMethod(ticketGenerationService,
                "isTicketDepartureDateAfterUserDepartureDate", ticketDto, userRouteDto);

        assertFalse(result);
    }

    @Test
    void whenIsTicketDepartureDateEqualUserDepartureDate_AndUserDepartureTimeIsNull() throws Exception {
        TicketDto ticketDto = testObjectFactory.getTicketDto();
        ticketDto.setDepartureDate(LocalDateTime.of(LocalDate.now(), LocalTime.of(14, 0)));

        UserRouteDto userRouteDto = testObjectFactory.getValidUserRouteDto();
        userRouteDto.setDepartureDate(LocalDate.now());
        userRouteDto.setDepartureTime(null);

        boolean result = invokeMethod(ticketGenerationService,
                "isTicketDepartureDateAfterUserDepartureDate", ticketDto, userRouteDto);

        assertTrue(result);
    }

    @Test
    void whenIsTicketDepartureDateEqualUserDepartureDate_AndUserDepartureDateIsNull() throws Exception {
        TicketDto ticketDto = testObjectFactory.getTicketDto();
        ticketDto.setDepartureDate(LocalDateTime.of(LocalDate.now().plusDays(1), LocalTime.of(14, 0)));

        UserRouteDto userRouteDto = testObjectFactory.getValidUserRouteDto();
        userRouteDto.setDepartureDate(null);
        userRouteDto.setDepartureTime(LocalTime.of(14, 30));

        boolean actual = invokeMethod(ticketGenerationService,
                "isTicketDepartureDateAfterUserDepartureDate", ticketDto, userRouteDto);

        assertFalse(actual);
    }
}
