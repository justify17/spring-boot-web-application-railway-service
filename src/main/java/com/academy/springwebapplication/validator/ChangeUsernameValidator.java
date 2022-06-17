package com.academy.springwebapplication.validator;

import com.academy.springwebapplication.dto.ChangedAccountDataDto;
import com.academy.springwebapplication.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
@RequiredArgsConstructor
public class ChangeUsernameValidator implements Validator {
    private final RegistrationService registrationService;

    @Override
    public boolean supports(Class<?> clazz) {
        return ChangedAccountDataDto.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        ChangedAccountDataDto changedAccountDataDto = (ChangedAccountDataDto) target;

        if(registrationService.isUserExisting(changedAccountDataDto.getNewUsername())){
            errors.rejectValue("username","error.username","User with this username already exists");
        }
    }
}
