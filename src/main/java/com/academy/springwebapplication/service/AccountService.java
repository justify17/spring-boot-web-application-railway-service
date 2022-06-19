package com.academy.springwebapplication.service;

import com.academy.springwebapplication.dto.ChangedPasswordDto;
import com.academy.springwebapplication.dto.ChangedUserInformationDto;
import com.academy.springwebapplication.dto.ChangedUsernameDto;

public interface AccountService {
    ChangedUserInformationDto getUserInformation(String username);

    void saveUserInformation(ChangedUserInformationDto changedUserInformationDto);

    void saveNewUsername(ChangedUsernameDto changedUsernameDto);

    void saveNewPassword(ChangedPasswordDto changedPasswordDto);
}
