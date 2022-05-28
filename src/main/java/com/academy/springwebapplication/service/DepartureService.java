package com.academy.springwebapplication.service;

import com.academy.springwebapplication.dto.DepartureDto;
import com.academy.springwebapplication.dto.StationDto;
import com.academy.springwebapplication.model.entity.Departure;
import com.academy.springwebapplication.model.entity.Route;

import java.util.List;

public interface DepartureService {
    List<DepartureDto> getDeparturesByStation(StationDto station);

    List<Departure> getDeparturesWithScheduleForRoute(Route route);

    void setStationSchedulesForDeparture(Departure departure);
}
