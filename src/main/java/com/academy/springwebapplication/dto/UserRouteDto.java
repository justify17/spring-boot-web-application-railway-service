package com.academy.springwebapplication.dto;

import lombok.Data;

@Data
public class UserRouteDto {
    private StationDto departureStation;
    private StationDto arrivalStation;
}
