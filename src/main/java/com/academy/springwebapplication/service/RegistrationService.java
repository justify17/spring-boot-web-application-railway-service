package com.academy.springwebapplication.service;

import com.academy.springwebapplication.dto.UserRegistrationDto;

public interface RegistrationService {
    boolean isUserExisting(String username);

    void saveNewUser(UserRegistrationDto userRegistrationDto);
}
