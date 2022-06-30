package com.academy.springwebapplication.validator;

import com.academy.springwebapplication.annotation.UsernameNotTaken;
import com.academy.springwebapplication.model.entity.User;
import com.academy.springwebapplication.model.repository.UserRepository;
import lombok.RequiredArgsConstructor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@RequiredArgsConstructor
public class UsernameValidator implements ConstraintValidator<UsernameNotTaken, String> {
    private final UserRepository userRepository;

    @Override
    public boolean isValid(String newUsername, ConstraintValidatorContext constraintValidatorContext) {

        return !isUserExisting(newUsername);
    }

    private boolean isUserExisting(String username) {
        User existingUser = userRepository.findByUsername(username);

        return existingUser != null;
    }
}
