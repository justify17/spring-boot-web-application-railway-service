package com.academy.springwebapplication.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class TicketDto {
    private DepartureDto departure;
    private UserDto user;
    private StationDto departureStation;
    private LocalDateTime userDepartureDate;
    private StationDto arrivalStation;
    private LocalDateTime userArrivalDate;
    private String carriageComfortLevel;
    private Integer carriageNumber;
    private Integer seatNumber;
    private Integer routePrice;
    private Integer additionalPrice;
    private Double finalPrice;
}