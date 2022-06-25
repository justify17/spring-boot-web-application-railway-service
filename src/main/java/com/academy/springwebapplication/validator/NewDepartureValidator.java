package com.academy.springwebapplication.validator;

import com.academy.springwebapplication.annotation.NewDepartureConstraint;
import com.academy.springwebapplication.dto.DepartureDto;
import com.academy.springwebapplication.dto.RouteDto;
import com.academy.springwebapplication.dto.TrainDto;
import com.academy.springwebapplication.model.entity.Route;
import com.academy.springwebapplication.model.entity.Train;
import com.academy.springwebapplication.model.repository.RouteRepository;
import com.academy.springwebapplication.model.repository.TrainRepository;
import lombok.RequiredArgsConstructor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class NewDepartureValidator implements ConstraintValidator<NewDepartureConstraint, DepartureDto> {
    private final TrainRepository trainRepository;
    private final RouteRepository routeRepository;

    @Override
    public boolean isValid(DepartureDto departureDto, ConstraintValidatorContext constraintValidatorContext) {
        boolean valid = true;

        String trainType = getTrainType(departureDto.getTrain());
        String routeType = getRouteType(departureDto.getRoute());

        if (trainType == null || routeType == null) {

            return false;
        }

        switch (trainType) {
            case "suburban":

                valid = routeType.equals("regional");
                break;
            case "local":

                valid = routeType.equals("interregional");
                break;
            case "distant":

                valid = routeType.equals("interregional_long") || routeType.equals("international");
                break;
        }

        if (!valid) {
            constraintValidatorContext.disableDefaultConstraintViolation();

            constraintValidatorContext.buildConstraintViolationWithTemplate
                            (String.format("A %s type train cannot be used for a %s type route", trainType, routeType))
                    .addConstraintViolation();
        }

        return valid;
    }

    private String getTrainType(TrainDto trainDto) {
        Optional<Train> trainForNewDeparture = trainRepository.findById(trainDto.getId());

        return trainForNewDeparture.map(Train::getType).orElse(null);
    }

    private String getRouteType(RouteDto routeDto) {
        Optional<Route> routeForNewDeparture = routeRepository.findById(routeDto.getId());

        return routeForNewDeparture.map(Route::getType).orElse(null);
    }
}
