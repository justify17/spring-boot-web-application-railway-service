package com.academy.springwebapplication.mapper;

import com.academy.springwebapplication.dto.RouteStationDto;
import com.academy.springwebapplication.model.entity.RouteStation;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RouteStationMapper {
    RouteStationDto routeStationToRouteStationDto(RouteStation routeStation);
}
