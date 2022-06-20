package com.academy.springwebapplication.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.util.List;

@Data
public class RouteDto {

    @Positive(message = "The field train id must be positive")
    private int id;

    private String type;
    private List<RouteStationDto> routeStations;
}
