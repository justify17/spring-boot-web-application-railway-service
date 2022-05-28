package com.academy.springwebapplication.mapper;

import com.academy.springwebapplication.dto.RouteDto;
import com.academy.springwebapplication.model.entity.Route;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RouteMapper {
    default RouteDto routeToRouteDto(Route route) {
        RouteDto routeDto = new RouteDto();

        switch (route.getType()) {
            case "regional":
                routeDto.setType("Региональные линии");
                break;
            case "interregional":
                routeDto.setType("Межрегиональные линии");
                break;
            case "interregional_long":
                routeDto.setType("Межрегиональные линии дальнего следования");
                break;
            case "international":
                routeDto.setType("Международные линии");
                break;
            default:
                routeDto.setType("-");

        }

        return routeDto;
    }
}
