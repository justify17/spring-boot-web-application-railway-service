package com.academy.springwebapplication.service.impl;

import com.academy.springwebapplication.dto.DepartureDto;
import com.academy.springwebapplication.dto.StationDto;
import com.academy.springwebapplication.dto.StationSchedule;
import com.academy.springwebapplication.mapper.DepartureMapper;
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
    public List<Departure> getDeparturesWithScheduleForRoute(Route route) {
        List<Departure> commonDeparturesForTwoStations =
                getCommonDeparturesForStations(route.getDepartureStation(), route.getArrivalStation());

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

    private List<Departure> getCommonDeparturesForStations(Station firstStation, Station secondStation) {
        List<Departure> departuresByFirstStation = departureRepository.
                findByRoute_RouteStations_Station_TitleIgnoreCase(firstStation.getTitle());
        List<Departure> departuresBySecondStation = departureRepository.
                findByRoute_RouteStations_Station_TitleIgnoreCase(secondStation.getTitle());

        return departuresByFirstStation.stream()
                .filter(departuresBySecondStation::contains)
                .collect(Collectors.toList());
    }


    @Override
    public void setStationSchedulesForDeparture(Departure departure) {
        List<RouteStation> routeStations = departure.getRoute().getRouteStations();

        LocalDateTime arrivalDateAtNextStation = departure.getDepartureDate();
        for (RouteStation routeStation : routeStations) {
            StationSchedule stationSchedule = new StationSchedule();

           /* stationSchedule.setStation(routeStation.getStation());*/
            stationSchedule.setArrivalDate(arrivalDateAtNextStation);
            stationSchedule.setDepartureDate(stationSchedule.getArrivalDate().plusMinutes(routeStation.getStopMinutes()));

            departure.getStationSchedules().add(stationSchedule);

            arrivalDateAtNextStation = stationSchedule.getDepartureDate().plusMinutes(routeStation.getMinutesToNextStation());
        }

        departure.getStationSchedules().get(0).setArrivalDate(null);
        departure.getStationSchedules().get(departure.getStationSchedules().size() - 1).setDepartureDate(null);
    }
}
