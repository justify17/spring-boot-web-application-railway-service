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
    private int carriageNumber;
    private int seatNumber;
    private int routePrice;
    private int additionalPrice;
    private double finalPrice;
}