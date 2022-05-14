package com.academy.springwebapplication.model;

import com.academy.springwebapplication.model.entity.Station;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class StationSchedule {
    private Station station;
    private LocalDateTime departureDate;
    private LocalDateTime arrivalDate;
}
