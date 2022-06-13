package com.academy.springwebapplication.service;

import com.academy.springwebapplication.dto.ChangedAccountDataDto;
import com.academy.springwebapplication.dto.ChangedUserInformationDto;

public interface AccountService {
    ChangedUserInformationDto getUserInformation(String username);

    void saveUserInformation(ChangedUserInformationDto changedUserInformationDto);

    void saveNewUsername(ChangedAccountDataDto changedAccountDataDto);

    void saveNewPassword(ChangedAccountDataDto changedAccountDataDto);
}
