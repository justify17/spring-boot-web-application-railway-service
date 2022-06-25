package com.academy.springwebapplication.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RouteStationDto {
    private StationDto station;
    private int routeStopNumber;
    private LocalDateTime departureDate;
    private LocalDateTime arrivalDate;
}
