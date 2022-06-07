package com.academy.springwebapplication.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserRouteDto {
    private StationDto departureStation;
    private StationDto arrivalStation;
}
