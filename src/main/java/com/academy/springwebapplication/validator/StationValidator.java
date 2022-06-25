package com.academy.springwebapplication.validator;

import com.academy.springwebapplication.annotation.StationExists;
import com.academy.springwebapplication.model.entity.Station;
import com.academy.springwebapplication.model.repository.StationRepository;
import lombok.RequiredArgsConstructor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@RequiredArgsConstructor
public class StationValidator implements ConstraintValidator<StationExists, String> {
    private final StationRepository stationRepository;

    @Override
    public boolean isValid(String stationTitle, ConstraintValidatorContext constraintValidatorContext) {
        if (!stationTitle.trim().isEmpty()) {

            return isStationExisting(stationTitle);
        }

        return true;
    }

    public boolean isStationExisting(String stationTitle) {
        Station station = stationRepository.findByTitle(stationTitle);

        return station != null;
    }
}
