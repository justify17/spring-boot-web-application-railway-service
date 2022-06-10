package com.academy.springwebapplication.service.impl;

import com.academy.springwebapplication.dto.*;
import com.academy.springwebapplication.mapper.DepartureMapper;
import com.academy.springwebapplication.model.entity.Departure;
import com.academy.springwebapplication.model.repository.DepartureRepository;
import com.academy.springwebapplication.model.repository.RouteStationRepository;
import com.academy.springwebapplication.service.DepartureService;
import com.academy.springwebapplication.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.jni.Local;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DepartureServiceImpl implements DepartureService {
    private final DepartureRepository departureRepository;
    private final RouteStationRepository routeStationRepository;
    private final TicketService ticketService;
    private final DepartureMapper departureMapper;

    @Override
    public List<DepartureDto> getDeparturesByStation(StationDto stationDto) {
        String stationTitle = stationDto.getTitle();

        List<Departure> departuresByStation = departureRepository.
                findByRoute_RouteStations_Station_TitleIgnoreCase(stationTitle);

        return departuresByStation.stream()
                .map(departureMapper::departureToDepartureDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<Departure> getDeparturesForRoute(UserRouteDto userRouteDto) {
        List<Departure> commonDeparturesForTwoStations =
                getCommonDeparturesForStations(userRouteDto.getDepartureStation(), userRouteDto.getArrivalStation());

        return commonDeparturesForTwoStations.stream()
                .filter(departure ->
                        routeStationRepository.findByRoute_IdAndStation_Title(departure.getRoute().getId(),
                                userRouteDto.getDepartureStation().getTitle()).getRouteStopNumber()
                                <
                                routeStationRepository.findByRoute_IdAndStation_Title(departure.getRoute().getId(),
                                        userRouteDto.getArrivalStation().getTitle()).getRouteStopNumber())
                .collect(Collectors.toList());
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

    @Override
    public Departure getDepartureById(Integer id){

        return departureRepository.getById(id);
    }

    @Override
    public List<Seat> getCarriageSeatsForDeparture(DepartureDto departureDto, int carriageNumber) {
        List<Seat> seats = new ArrayList<>();

        CarriageDto carriageDto = departureDto.getTrain().getCarriages().stream()
                .filter(carriage -> carriage.getNumber() == carriageNumber)
                .findFirst().get();

        int numberOfSeats = carriageDto.getNumberOfSeats();

        for (int i = 1; i <= numberOfSeats; i++) {
            Seat seat = new Seat();

            seat.setCarriageNumber(carriageNumber);
            seat.setNumber(i);

            boolean isFree = ticketService.isTicketExists(departureDto, seat) ? false : true;
            seat.setFree(isFree);

            seats.add(seat);
        }

        return seats;
    }
}
