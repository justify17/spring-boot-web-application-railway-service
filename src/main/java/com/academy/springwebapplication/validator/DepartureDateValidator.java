package com.academy.springwebapplication.validator;

import com.academy.springwebapplication.annotation.DepartureDateConstraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class DepartureDateValidator implements ConstraintValidator<DepartureDateConstraint, LocalDate> {

    @Override
    public boolean isValid(LocalDate departureDate, ConstraintValidatorContext constraintValidatorContext) {
        if (departureDate == null) {

            return true;
        }

        LocalDate maximumValidDate = LocalDate.now().plusMonths(3);

        return departureDate.isBefore(maximumValidDate);
    }
}
