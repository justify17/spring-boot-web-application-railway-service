package com.academy.springwebapplication.validator;

import com.academy.springwebapplication.annotation.UsernameNotTaken;
import com.academy.springwebapplication.service.RegistrationService;
import lombok.RequiredArgsConstructor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@RequiredArgsConstructor
public class UsernameValidator implements ConstraintValidator<UsernameNotTaken, String> {
    private final RegistrationService registrationService;

    @Override
    public boolean isValid(String newUsername, ConstraintValidatorContext constraintValidatorContext) {

        return !registrationService.isUserExisting(newUsername);
    }
}
