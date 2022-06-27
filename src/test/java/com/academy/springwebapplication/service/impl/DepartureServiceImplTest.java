package com.academy.springwebapplication.service.impl;

import com.academy.springwebapplication.dto.DepartureDto;
import com.academy.springwebapplication.dto.StationDto;
import com.academy.springwebapplication.exception.EntityByIdNotFoundException;
import com.academy.springwebapplication.mapper.DepartureMapper;
import com.academy.springwebapplication.model.entity.Departure;
import com.academy.springwebapplication.model.entity.Route;
import com.academy.springwebapplication.model.entity.Train;
import com.academy.springwebapplication.model.repository.DepartureRepository;
import com.academy.springwebapplication.model.repository.RouteRepository;
import com.academy.springwebapplication.model.repository.TrainRepository;
import com.academy.springwebapplication.service.TicketService;
import com.academy.springwebapplication.util.TestObjectFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@SpringBootTest
class DepartureServiceImplTest {

    @InjectMocks
    private DepartureServiceImpl departureService;

    @InjectMocks
    private TestObjectFactory testObjectFactory;

    @Mock
    private DepartureRepository departureRepository;

    @Mock
    private TrainRepository trainRepository;

    @Mock
    private RouteRepository routeRepository;

    @Mock
    private TicketService ticketService;

    @Mock
    private DepartureMapper departureMapper;

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
        firstDeparture.setId(1);
        firstDeparture.getRoute().getRouteStations().get(0).setRouteStopNumber(2);
        firstDeparture.getRoute().getRouteStations().get(1).setRouteStopNumber(1);

        Departure secondDeparture = testObjectFactory.getDeparture();
        secondDeparture.setId(2);
        secondDeparture.getRoute().getRouteStations().get(0).setRouteStopNumber(1);
        secondDeparture.getRoute().getRouteStations().get(1).setRouteStopNumber(2);

        List<Departure> departures = List.of(firstDeparture, secondDeparture);

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

    @Test
    void whenGetCommonDeparturesForStations() throws Exception {
        StationDto firstStation = new StationDto();
        firstStation.setTitle("Гомель");

        StationDto secondStation = new StationDto();
        firstStation.setTitle("Минск");

        Departure departure1 = new Departure();
        departure1.setId(1);
        departure1.setDepartureDate(LocalDateTime.now().plusDays(3));

        Departure departure2 = new Departure();
        departure2.setId(2);
        departure2.setDepartureDate(LocalDateTime.now().plusDays(2));

        Departure departure3 = new Departure();
        departure3.setId(3);
        departure3.setDepartureDate(LocalDateTime.now().plusDays(1));

        List<Departure> departureByFirstStation = List.of(departure1, departure2);
        List<Departure> departureBySecondStation = List.of(departure1, departure2, departure3);

        PowerMockito.when(departureRepository.findByRoute_RouteStations_Station_TitleIgnoreCase(firstStation.getTitle()))
                .thenReturn(departureByFirstStation);
        PowerMockito.when(departureRepository.findByRoute_RouteStations_Station_TitleIgnoreCase(secondStation.getTitle()))
                .thenReturn(departureBySecondStation);

        List<Departure> result = List.of(departure2, departure1);

        List<Departure> actual = Whitebox.invokeMethod(departureService, "getCommonDeparturesForStations",
                firstStation, secondStation);

        Assertions.assertEquals(result, actual);

        Mockito.verify(departureRepository, times(1))
                .findByRoute_RouteStations_Station_TitleIgnoreCase(firstStation.getTitle());

        Mockito.verify(departureRepository, times(1))
                .findByRoute_RouteStations_Station_TitleIgnoreCase(secondStation.getTitle());
    }

    @Test
    void whenGetDepartureById() {
        Integer id = 10;

        Departure result = new Departure();
        result.setId(id);

        when(departureRepository.getById(id)).thenReturn(result);

        Assertions.assertEquals(result, departureService.getDepartureById(id));

        verify(departureRepository, times(1)).getById(id);
    }

    @Test
    void whenSaveNewDeparture() {
        DepartureDto departureDto = testObjectFactory.getDepartureDto();

        Train train = new Train();
        train.setId(departureDto.getTrain().getId());

        when(trainRepository.getById(departureDto.getTrain().getId())).thenReturn(train);

        Route route = new Route();
        route.setId(departureDto.getRoute().getId());

        when(routeRepository.getById(departureDto.getRoute().getId())).thenReturn(route);

        Departure result = new Departure();
        result.setTrain(train);
        result.setRoute(route);
        result.setDepartureDate(departureDto.getDepartureDate());

        departureService.saveNewDeparture(departureDto);

        verify(trainRepository, times(1)).getById(departureDto.getTrain().getId());
        verify(routeRepository, times(1)).getById(departureDto.getRoute().getId());
        verify(departureRepository, times(1)).save(result);
    }

    @Test
    void whenDeleteDepartureById() {
        Integer departureId = 1;

        departureService.deleteDepartureById(departureId);

        verify(departureRepository, times(1)).deleteById(departureId);
    }

    @Test
    void whenCheckIfDepartureIdIsValid_AndDepartureIsNotExisting() {
        Integer departureId = 0;

        when(departureRepository.existsById(departureId)).thenReturn(false);

        assertThrows(EntityByIdNotFoundException.class, () -> departureService.checkIfDepartureIdIsValid(departureId));

        verify(departureRepository, times(1)).existsById(departureId);
    }

    @Test
    void whenCheckIfDepartureIdIsValid_AndDepartureIsExisting() {
        Integer departureId = 10;

        when(departureRepository.existsById(departureId)).thenReturn(true);

        departureService.checkIfDepartureIdIsValid(departureId);

        verify(departureRepository, times(1)).existsById(departureId);
    }
}
