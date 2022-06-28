package com.academy.springwebapplication.service;

import com.academy.springwebapplication.dto.TicketDto;
import com.academy.springwebapplication.dto.UserRouteDto;
import com.academy.springwebapplication.model.entity.Departure;

import java.util.List;

public interface TicketGenerationService {
    List<TicketDto> generateTicketsSuitableForUserRoute(List<Departure> departures, UserRouteDto userRouteDto);

    TicketDto createTicketForDepartureAlongRoute(Departure departure, UserRouteDto userRouteDto);
}
