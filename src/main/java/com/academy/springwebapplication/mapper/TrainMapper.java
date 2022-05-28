package com.academy.springwebapplication.mapper;

import com.academy.springwebapplication.dto.TrainDto;
import com.academy.springwebapplication.model.entity.Train;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TrainMapper {
    TrainDto trainToTrainDto(Train train);
}
