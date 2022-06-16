package com.academy.springwebapplication.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RouteStationDto {
    private StationDto station;
    private int routeStopNumber;
    private LocalDateTime departureDate;
    private LocalDateTime arrivalDate;
}
