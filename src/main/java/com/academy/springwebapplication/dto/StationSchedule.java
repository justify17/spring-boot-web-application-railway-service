package com.academy.springwebapplication.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class StationSchedule {
    private StationDto station;
    private LocalDateTime departureDate;
    private LocalDateTime arrivalDate;
}
