package com.academy.springwebapplication.service;

import com.academy.springwebapplication.dto.UserDto;

public interface RegistrationService {
    boolean isUserExists(UserDto userDto);

    void saveNewUser(UserDto userDto);
}
