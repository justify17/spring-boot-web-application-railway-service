package com.academy.springwebapplication.dto;

import lombok.Data;

import java.util.List;

@Data
public class RouteDto {
    private Integer id;
    private String type;
    private List<RouteStationDto> routeStations;
}
