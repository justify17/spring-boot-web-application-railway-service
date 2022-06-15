package com.academy.springwebapplication.mapper;

import com.academy.springwebapplication.dto.RouteDto;
import com.academy.springwebapplication.model.entity.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public abstract class RouteMapperDecorator implements RouteMapper {
    @Autowired
    @Qualifier("delegate")
    private RouteMapper delegate;

    @Override
    public RouteDto routeToRouteDto(Route route) {
        RouteDto routeDto = delegate.routeToRouteDto(route);
        routeDto.setType(getType(route));

        return routeDto;
    }

    private String getType(Route route) {
        switch (route.getType()) {
            case "regional":

                return "Региональные линии";
            case "interregional":

                return "Межрегиональные линии";
            case "interregional_long":

                return "Межрегиональные линии дальнего следования";
            case "international":

                return "Международные линии";
            default:

                return "-";
        }
    }
}
