package com.academy.springwebapplication.service.impl;

import com.academy.springwebapplication.dto.*;
import com.academy.springwebapplication.exception.EntityByIdNotFoundException;
import com.academy.springwebapplication.exception.EntityByTitleNotFoundException;
import com.academy.springwebapplication.mapper.DepartureMapper;
import com.academy.springwebapplication.model.entity.Departure;
import com.academy.springwebapplication.model.entity.Route;
import com.academy.springwebapplication.model.entity.Station;
import com.academy.springwebapplication.model.entity.Train;
import com.academy.springwebapplication.model.repository.*;
import com.academy.springwebapplication.service.DepartureService;
import com.academy.springwebapplication.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class is responsible for receiving information about departures and managing them
 */
@Service
@RequiredArgsConstructor
public class DepartureServiceImpl implements DepartureService {
    private final DepartureRepository departureRepository;
    private final RouteStationRepository routeStationRepository;
    private final TrainRepository trainRepository;
    private final RouteRepository routeRepository;
    private final TicketService ticketService;
    private final DepartureMapper departureMapper;

    /**
     * Returns all departures from the database
     *
     * @return all departures
     */
    @Override
    public List<DepartureDto> getAllDepartures() {
        List<Departure> departures = departureRepository.findAll();

        return departures.stream()
                .map(departureMapper::departureToDepartureDto)
                .sorted(Comparator.comparing(DepartureDto::getDepartureDate))
                .peek(departureDto ->
                        departureDto.setPurchasedTickets(
                                ticketService.getNumberOfPurchasedTicketsForDeparture(departureDto.getId())))
                .collect(Collectors.toList());
    }

    /**
     * Returns all departures from the database by station
     *
     * @param stationDto the station to find departures
     * @return departures by station
     */
    @Override
    public List<DepartureDto> getDeparturesByStation(StationDto stationDto) {
        String stationTitle = stationDto.getTitle();

        List<Departure> departuresByStation = departureRepository.
                findByRoute_RouteStations_Station_TitleIgnoreCase(stationTitle);

        return departuresByStation.stream()
                .map(departureMapper::departureToDepartureDto)
                .sorted(Comparator.comparing(DepartureDto::getDepartureDate))
                .collect(Collectors.toList());
    }

    /**
     * Returns all departures from the database for route
     *
     * @param userRouteDto the route to find departures
     * @return departures for route
     */
    @Override
    public List<Departure> getDeparturesForRoute(UserRouteDto userRouteDto) {
        List<Departure> commonDeparturesForTwoStations =
                getCommonDeparturesForStations(userRouteDto.getDepartureStation(), userRouteDto.getArrivalStation());

        return commonDeparturesForTwoStations.stream()
                .filter(departure -> {
                    int userDepartureStationStopNumber = routeStationRepository.findByRoute_IdAndStation_Title
                                    (departure.getRoute().getId(), userRouteDto.getDepartureStation().getTitle())
                            .getRouteStopNumber();

                    int userArrivalStationStopNumber = routeStationRepository.findByRoute_IdAndStation_Title
                                    (departure.getRoute().getId(), userRouteDto.getArrivalStation().getTitle())
                            .getRouteStopNumber();

                    return userDepartureStationStopNumber < userArrivalStationStopNumber;
                })
                .collect(Collectors.toList());
    }

    /**
     * Returns common departures for two stations
     *
     * @param firstStation  the station to search for common departures with second station
     * @param secondStation the station to search for common departures with first station
     * @return common departures for two stations
     */
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

    /**
     * Returns the departure with the given id from the database
     *
     * @param id the departure id
     * @return the departure
     */
    @Override
    public Departure getDepartureById(Integer id) {

        return departureRepository.getById(id);
    }

    /**
     * Saves the departure in the database
     *
     * @param departureDto the departure to save
     */
    @Override
    public void saveNewDeparture(DepartureDto departureDto) {
        Departure departure = new Departure();

        Train train = trainRepository.getById(departureDto.getTrain().getId());
        departure.setTrain(train);

        Route route = routeRepository.getById(departureDto.getRoute().getId());
        departure.setRoute(route);

        departure.setDepartureDate(departureDto.getDepartureDate());

        departureRepository.save(departure);
    }

    /**
     * Deletes the departure with the given id from the database
     *
     * @param departureId departure id
     */
    @Override
    public void deleteDepartureById(Integer departureId) {
        departureRepository.deleteById(departureId);
    }

    /**
     * Checks for the presence of the departure with the given ID in the database
     *
     * @param departureId departure id
     */
    @Override
    public void checkIfDepartureIdIsValid(Integer departureId) {
        if (!departureRepository.existsById(departureId)) {

            throw new EntityByIdNotFoundException(departureId);
        }
    }
}
