package com.academy.springwebapplication.service;

import com.academy.springwebapplication.model.entity.User;

public interface RegistrationService {
    boolean isUserExists(User user);

    void saveNewUser(User user);
}
