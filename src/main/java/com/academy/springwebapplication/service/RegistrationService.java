package com.academy.springwebapplication.service;

import com.academy.springwebapplication.dto.UserRegistrationDto;

public interface RegistrationService {
    boolean isUserExists(String username);

    void saveNewUser(UserRegistrationDto userRegistrationDto);
}
