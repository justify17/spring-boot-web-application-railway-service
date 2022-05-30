package com.academy.springwebapplication.mapper;

import com.academy.springwebapplication.dto.DepartureDto;
import com.academy.springwebapplication.model.entity.Departure;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {RouteMapper.class, TrainMapper.class})
@DecoratedWith(DepartureMapperDecorator.class)
public interface DepartureMapper {
    DepartureDto departureToDepartureDto(Departure departure);
}
