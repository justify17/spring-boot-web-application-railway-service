package com.academy.springwebapplication.service.impl;

import com.academy.springwebapplication.model.entity.Departure;
import com.academy.springwebapplication.model.entity.Route;
import com.academy.springwebapplication.model.entity.Station;
import com.academy.springwebapplication.model.repository.DepartureRepository;
import com.academy.springwebapplication.model.repository.RouteStationRepository;
import com.academy.springwebapplication.service.DepartureService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DepartureServiceImpl implements DepartureService {
    private final DepartureRepository departureRepository;
    private final RouteStationRepository routeStationRepository;

    @Override
    public List<Departure> getDeparturesByStation(Station station) {
        String stationTitle = station.getTitle();

        return departureRepository.findByRoute_RouteStations_Station_TitleIgnoreCase(stationTitle);
    }

    @Override
    public List<Departure> getDeparturesForRoute(Route route) {
        List<Departure> commonDeparturesForTwoStations =
                getCommonDeparturesForTwoStations(route.getDepartureStation(), route.getArrivalStation());

        return commonDeparturesForTwoStations.stream()
                .filter(departure ->
                        routeStationRepository.findByRoute_IdAndStation_Title(departure.getRoute().getId(),
                                route.getDepartureStation().getTitle()).getRouteStopNumber()
                                <
                        routeStationRepository.findByRoute_IdAndStation_Title(departure.getRoute().getId(),
                                route.getArrivalStation().getTitle()).getRouteStopNumber())
                .collect(Collectors.toList());
    }

    private List<Departure> getCommonDeparturesForTwoStations(Station firstStation, Station secondStation){
        List<Departure> departuresByFirstStation = getDeparturesByStation(firstStation);
        List<Departure> departuresBySecondStation = getDeparturesByStation(secondStation);

        return departuresByFirstStation.stream()
                .filter(departuresBySecondStation::contains)
                .collect(Collectors.toList());
    }
}
