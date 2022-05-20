package com.academy.springwebapplication.service;

import com.academy.springwebapplication.model.entity.Departure;
import com.academy.springwebapplication.model.entity.Route;
import com.academy.springwebapplication.model.entity.Station;

import java.util.List;

public interface DepartureService {
    List<Departure> getDeparturesByStation(Station station);

    List<Departure> getDeparturesWithScheduleForRoute(Route route);

    void setStationSchedulesForDeparture(Departure departure);
}
