package com.academy.springwebapplication.service;

import com.academy.springwebapplication.dto.UserRegistrationDto;

public interface RegistrationService {
    void saveNewUser(UserRegistrationDto userRegistrationDto);
}
