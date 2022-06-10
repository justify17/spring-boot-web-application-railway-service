package com.academy.springwebapplication.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TicketDto {
    private Integer id;
    private DepartureDto departure;
    private String username;
    private StationDto departureStation;
    private LocalDateTime departureDate;
    private StationDto arrivalStation;
    private LocalDateTime arrivalDate;
    private String carriageComfortLevel;
    private Integer carriageNumber;
    private Integer seatNumber;
    private Integer routePrice;
    private Integer additionalPrice;
    private Double finalPrice;
}