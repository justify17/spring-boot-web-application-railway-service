package com.academy.springwebapplication.service.impl;

import com.academy.springwebapplication.dto.DepartureDto;
import com.academy.springwebapplication.dto.StationDto;
import com.academy.springwebapplication.mapper.DepartureMapper;
import com.academy.springwebapplication.model.entity.Departure;
import com.academy.springwebapplication.model.repository.DepartureRepository;
import com.academy.springwebapplication.model.repository.RouteRepository;
import com.academy.springwebapplication.model.repository.RouteStationRepository;
import com.academy.springwebapplication.model.repository.TrainRepository;
import com.academy.springwebapplication.service.TicketService;
import com.academy.springwebapplication.util.TestObjectFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
class DepartureServiceImplTest {

    @InjectMocks
    private DepartureServiceImpl departureService;

    @Mock
    private DepartureRepository departureRepository;

    @Mock
    private RouteStationRepository routeStationRepository;

    @Mock
    private TrainRepository trainRepository;

    @Mock
    private RouteRepository routeRepository;

    @Mock
    private TicketService ticketService;

    @Mock
    private DepartureMapper departureMapper;

    @Spy
    private TestObjectFactory testObjectFactory;

    @Test
    void whenGetAllDepartures() {
        Departure firstDeparture = new Departure();
        firstDeparture.setId(1);

        Departure secondDeparture = new Departure();
        secondDeparture.setId(2);

        List<Departure> departures = List.of(firstDeparture, secondDeparture);

        when(departureRepository.findAll()).thenReturn(departures);

        DepartureDto firstDepartureDto = new DepartureDto();
        firstDepartureDto.setId(1);
        firstDepartureDto.setDepartureDate(LocalDateTime.now().plusDays(2));

        DepartureDto secondDepartureDto = new DepartureDto();
        secondDepartureDto.setId(2);
        secondDepartureDto.setDepartureDate(LocalDateTime.now().plusDays(1));

        when(departureMapper.departureToDepartureDto(firstDeparture)).thenReturn(firstDepartureDto);
        when(departureMapper.departureToDepartureDto(secondDeparture)).thenReturn(secondDepartureDto);

        List<DepartureDto> result = List.of(secondDepartureDto, firstDepartureDto);

        int numberOfTicketsForFirstDepartureDto = 30;
        int numberOfTicketsForSecondDepartureDto = 50;

        when(ticketService.getNumberOfPurchasedTicketsForDeparture(firstDepartureDto.getId()))
                .thenReturn(numberOfTicketsForFirstDepartureDto);
        when(ticketService.getNumberOfPurchasedTicketsForDeparture(secondDepartureDto.getId()))
                .thenReturn(numberOfTicketsForSecondDepartureDto);

        assertEquals(result, departureService.getAllDepartures());

        assertEquals(numberOfTicketsForFirstDepartureDto, firstDepartureDto.getPurchasedTickets());
        assertEquals(numberOfTicketsForSecondDepartureDto, secondDepartureDto.getPurchasedTickets());

        verify(departureRepository, times(1)).findAll();
        verify(departureMapper, times(1)).departureToDepartureDto(firstDeparture);
        verify(departureMapper, times(1)).departureToDepartureDto(secondDeparture);
        verify(ticketService, times(1)).getNumberOfPurchasedTicketsForDeparture(firstDeparture.getId());
        verify(ticketService, times(1)).getNumberOfPurchasedTicketsForDeparture(secondDeparture.getId());
    }

    @Test
    void whenGetDeparturesByStation() {
        StationDto stationDto = new StationDto();
        stationDto.setTitle("Гомель");

        Departure departure = new Departure();
        departure.setId(1);

        List<Departure> departures = List.of(departure);

        when(departureRepository.findByRoute_RouteStations_Station_TitleIgnoreCase(stationDto.getTitle()))
                .thenReturn(departures);

        DepartureDto departureDto = new DepartureDto();
        departureDto.setId(1);

        when(departureMapper.departureToDepartureDto(departure)).thenReturn(departureDto);

        List<DepartureDto> result = List.of(departureDto);

        Assertions.assertEquals(result, departureService.getDeparturesByStation(stationDto));

        verify(departureRepository, times(1))
                .findByRoute_RouteStations_Station_TitleIgnoreCase(stationDto.getTitle());
        verify(departureMapper, times(1)).departureToDepartureDto(departure);
    }

    @Test
    void whenGetDeparturesForRoute() {
        Departure firstDeparture = testObjectFactory.getDeparture();
        Departure secondDeparture = testObjectFactory.getDeparture();

        List<Departure> departures = List.of(firstDeparture, secondDeparture);

        firstDeparture.getRoute().getRouteStations().get(0).setRouteStopNumber(2);
        firstDeparture.getRoute().getRouteStations().get(1).setRouteStopNumber(1);

        secondDeparture.getRoute().getRouteStations().get(0).setRouteStopNumber(1);
        secondDeparture.getRoute().getRouteStations().get(1).setRouteStopNumber(2);

        List<Departure> actual = departures.stream()
                .filter(departure -> {
                    int stopNumberDepartureStation = departure.getRoute().getRouteStations().get(0).getRouteStopNumber();
                    int stopNumberArrivalStation = departure.getRoute().getRouteStations().get(1).getRouteStopNumber();

                    return stopNumberDepartureStation < stopNumberArrivalStation;
                })
                .collect(Collectors.toList());

        List<Departure> result = List.of(secondDeparture);

        Assertions.assertEquals(result, actual);
    }

    private List<Departure> getCommonDeparturesForStations(StationDto firstStation, StationDto secondStation) {
        List<Departure> departuresByFirstStation = departureRepository.
                findByRoute_RouteStations_Station_TitleIgnoreCase(firstStation.getTitle());
        List<Departure> departuresBySecondStation = departureRepository.
                findByRoute_RouteStations_Station_TitleIgnoreCase(secondStation.getTitle());

        return departuresByFirstStation.stream()
                .filter(departuresBySecondStation::contains)
                .sorted(Comparator.comparing(Departure::getDepartureDate))
                .collect(Collectors.toList());
    }

    @Test
    void whenGetCommonDeparturesForStations() {

    }
}
