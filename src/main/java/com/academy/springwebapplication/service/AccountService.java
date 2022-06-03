package com.academy.springwebapplication.service;

import com.academy.springwebapplication.dto.UserInformationDto;

public interface AccountService {
    UserInformationDto getUserInformation(String username);

    void saveUserInformation(UserInformationDto userInformationDto, String username);
}
