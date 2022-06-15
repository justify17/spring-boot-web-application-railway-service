package com.academy.springwebapplication.service;

import com.academy.springwebapplication.dto.*;
import com.academy.springwebapplication.model.entity.Departure;
import com.academy.springwebapplication.model.entity.Route;

import java.util.List;

public interface DepartureService {
    void saveNewDeparture(DepartureDto departureDto);

    List<DepartureDto> getAllDepartures();

    List<DepartureDto> getDeparturesByStation(StationDto stationDto);

    List<Departure> getDeparturesForRoute(UserRouteDto userRouteDto);

    Departure getDepartureById(Integer id);

    List<Seat> getCarriageSeatsForDeparture(DepartureDto departureDto, int carriageNumber);
}
