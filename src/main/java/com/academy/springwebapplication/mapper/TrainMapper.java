package com.academy.springwebapplication.mapper;

import com.academy.springwebapplication.dto.TrainDto;
import com.academy.springwebapplication.mapper.decorator.TrainMapperDecorator;
import com.academy.springwebapplication.model.entity.Train;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
@DecoratedWith(TrainMapperDecorator.class)
public interface TrainMapper {
    TrainDto trainToTrainDto(Train train);
}
