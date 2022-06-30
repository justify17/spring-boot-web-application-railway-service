package com.academy.springwebapplication.service;

import com.academy.springwebapplication.dto.DepartureDto;
import com.academy.springwebapplication.dto.StationDto;
import com.academy.springwebapplication.dto.UserRouteDto;
import com.academy.springwebapplication.model.entity.Departure;

import java.util.List;

public interface DepartureService {
    void saveNewDeparture(DepartureDto departureDto);

    void deleteDepartureById(Integer departureId);

    List<DepartureDto> getAllDepartures();

    List<DepartureDto> getDeparturesByStation(StationDto stationDto);

    List<Departure> getDeparturesForRoute(UserRouteDto userRouteDto);

    Departure getDepartureById(Integer id);

    void checkIfDepartureIdIsValid(Integer departureId);
}
