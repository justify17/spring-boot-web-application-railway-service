package com.academy.springwebapplication.validator;

import com.academy.springwebapplication.dto.StationDto;
import com.academy.springwebapplication.dto.UserRouteDto;
import com.academy.springwebapplication.model.entity.Station;
import com.academy.springwebapplication.model.repository.StationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class UserRouteValidator implements Validator {
    private final StationRepository stationRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return UserRouteDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserRouteDto userRouteDto = (UserRouteDto) target;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors,
                "departureStation.title", "required.departureStation",
                "Field Departure Station is required!");

        if (!errors.hasFieldErrors("departureStation.title")) {
            if (isStationNotExisting(userRouteDto.getDepartureStation())) {
                errors.rejectValue("departureStation.title", "error.departureStation",
                        String.format(("Station %s does not exist!"), userRouteDto.getDepartureStation().getTitle()));
            }
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors,
                "arrivalStation.title", "required.arrivalStation",
                "Field Arrival Station is required!");

        if (!errors.hasFieldErrors("arrivalStation.title")) {
            if (isStationNotExisting(userRouteDto.getArrivalStation())) {
                errors.rejectValue("arrivalStation.title", "error.arrivalStation",
                        String.format(("Station %s does not exist!"), userRouteDto.getArrivalStation().getTitle()));
            }
        }
    }

    private boolean isStationNotExisting(StationDto stationDto) {
        Station station = stationRepository.findByTitle(stationDto.getTitle());

        return station == null;
    }
}
