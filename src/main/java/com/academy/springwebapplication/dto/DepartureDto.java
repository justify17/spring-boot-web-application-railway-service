package com.academy.springwebapplication.dto;

import lombok.Data;

import java.util.List;

@Data
public class DepartureDto {
    private Integer id;
    private TrainDto train;
    private RouteDto route;
    private List<StationSchedule> stationSchedules;
}
