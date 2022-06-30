package com.academy.springwebapplication.mapper;

import com.academy.springwebapplication.dto.RouteDto;
import com.academy.springwebapplication.mapper.decorator.RouteMapperDecorator;
import com.academy.springwebapplication.model.entity.Route;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {RouteStationMapper.class})
@DecoratedWith(RouteMapperDecorator.class)
public interface RouteMapper {
    RouteDto routeToRouteDto(Route route);
}
