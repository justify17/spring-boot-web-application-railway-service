package com.academy.springwebapplication.service;

import com.academy.springwebapplication.dto.ChangedAccountDataDto;
import com.academy.springwebapplication.dto.UserInformationDto;

public interface AccountService {
    UserInformationDto getUserInformation(String username);

    void saveUserInformation(UserInformationDto userInformationDto);

    void saveNewUsername(ChangedAccountDataDto changedAccountDataDto);

    void saveNewPassword(ChangedAccountDataDto changedAccountDataDto);
}
