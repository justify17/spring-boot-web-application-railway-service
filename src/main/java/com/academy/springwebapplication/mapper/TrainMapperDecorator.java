package com.academy.springwebapplication.mapper;

import com.academy.springwebapplication.dto.CarriageDto;
import com.academy.springwebapplication.dto.TrainDto;
import com.academy.springwebapplication.model.entity.Train;
import com.academy.springwebapplication.model.entity.TrainCarriage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.ArrayList;
import java.util.List;

public abstract class TrainMapperDecorator implements TrainMapper {

    @Autowired
    @Qualifier("delegate")
    private TrainMapper delegate;

    @Override
    public TrainDto trainToTrainDto(Train train) {
        TrainDto trainDto = delegate.trainToTrainDto(train);
        trainDto.setCarriages(getCarriages(train));

        return trainDto;
    }

    private List<CarriageDto> getCarriages(Train train) {
        List<CarriageDto> carriages = new ArrayList<>();

        List<TrainCarriage> trainCarriages = train.getTrainCarriages();

        for (TrainCarriage trainCarriage : trainCarriages) {
            CarriageDto carriageDto = new CarriageDto();

            carriageDto.setComfortLevel(trainCarriage.getCarriage().getComfortLevel());
            carriageDto.setNumberOfSeats(trainCarriage.getCarriage().getSeats());
            carriageDto.setNumber(trainCarriage.getCarriageNumber());

            carriages.add(carriageDto);
        }

        return carriages;
    }
}
