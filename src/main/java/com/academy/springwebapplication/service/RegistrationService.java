package com.academy.springwebapplication.service;

import com.academy.springwebapplication.dto.UserDto;

public interface RegistrationService {
    boolean isUserExists(String username);

    void saveNewUser(UserDto userDto);
}
