package com.academy.springwebapplication.service.impl;

import com.academy.springwebapplication.model.StationSchedule;
import com.academy.springwebapplication.model.entity.Departure;
import com.academy.springwebapplication.model.entity.Route;
import com.academy.springwebapplication.model.entity.RouteStation;
import com.academy.springwebapplication.model.entity.Station;
import com.academy.springwebapplication.model.repository.DepartureRepository;
import com.academy.springwebapplication.model.repository.RouteStationRepository;
import com.academy.springwebapplication.service.DepartureService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DepartureServiceImpl implements DepartureService {
    private final DepartureRepository departureRepository;
    private final RouteStationRepository routeStationRepository;

    // изменить метод добавить расписание
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
                .peek(this::setStationSchedulesForDeparture)
                .collect(Collectors.toList());
    }

    private List<Departure> getCommonDeparturesForTwoStations(Station firstStation, Station secondStation) {
        List<Departure> departuresByFirstStation = getDeparturesByStation(firstStation);
        List<Departure> departuresBySecondStation = getDeparturesByStation(secondStation);

        return departuresByFirstStation.stream()
                .filter(departuresBySecondStation::contains)
                .collect(Collectors.toList());
    }

    private void setStationSchedulesForDeparture(Departure departure) {
        List<RouteStation> routeStations = departure.getRoute().getRouteStations();

        LocalDateTime arrivalDateAtNextStation = departure.getDepartureDate();
        for (RouteStation routeStation : routeStations) {
            StationSchedule stationSchedule = new StationSchedule();

            stationSchedule.setStation(routeStation.getStation());
            stationSchedule.setArrivalDate(arrivalDateAtNextStation);
            stationSchedule.setDepartureDate(stationSchedule.getArrivalDate().plusMinutes(routeStation.getStopMinutes()));

            departure.getStationSchedules().add(stationSchedule);

            arrivalDateAtNextStation = stationSchedule.getDepartureDate().plusMinutes(routeStation.getMinutesToNextStation());
        }
    }
}
