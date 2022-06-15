package com.academy.springwebapplication.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class RouteStationDto {
    private StationDto station;
    private Integer routeStopNumber;
    private LocalDateTime departureDate;
    private LocalDateTime arrivalDate;
}
