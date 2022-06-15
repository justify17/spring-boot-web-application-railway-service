package com.academy.springwebapplication.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class DepartureDto {
    private Integer id;
    private TrainDto train;
    private RouteDto route;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime departureDate;

    private Integer purchasedTickets;
}
