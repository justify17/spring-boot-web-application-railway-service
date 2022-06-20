package com.academy.springwebapplication.validator;

import com.academy.springwebapplication.annotation.NewDepartureConstraint;
import com.academy.springwebapplication.dto.DepartureDto;
import com.academy.springwebapplication.model.entity.Route;
import com.academy.springwebapplication.model.entity.Train;
import com.academy.springwebapplication.model.repository.RouteRepository;
import com.academy.springwebapplication.model.repository.TrainRepository;
import lombok.RequiredArgsConstructor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@RequiredArgsConstructor
public class NewDepartureValidator implements ConstraintValidator<NewDepartureConstraint, DepartureDto> {
    private final TrainRepository trainRepository;
    private final RouteRepository routeRepository;

    @Override
    public boolean isValid(DepartureDto departureDto, ConstraintValidatorContext constraintValidatorContext) {
        boolean valid = true;

        Train trainForNewDeparture = trainRepository.getById(departureDto.getTrain().getId());
        String trainType = trainForNewDeparture.getType();

        Route routeForNewDeparture = routeRepository.getById(departureDto.getRoute().getId());
        String routeType = routeForNewDeparture.getType();

        if (trainType.equals("suburban") && !routeType.equals("regional")) {

            valid = false;
        } else if (trainType.equals("local") && !routeType.equals("interregional")) {

            valid = false;
        } else if (trainType.equals("distant") && !routeType.equals("interregional_long")) {

            valid = false;
        }

        if (!valid) {
            constraintValidatorContext.disableDefaultConstraintViolation();

            constraintValidatorContext.buildConstraintViolationWithTemplate
                    (String.format("A %s type train cannot be used for a %s type route", trainType, routeType))
                    .addConstraintViolation();
        }

        return valid;
    }
}
