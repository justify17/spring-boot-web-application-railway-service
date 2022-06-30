package com.academy.springwebapplication.mapper.decorator;

import com.academy.springwebapplication.dto.ChangedUserInformationDto;
import com.academy.springwebapplication.mapper.UserInformationMapper;
import com.academy.springwebapplication.model.entity.UserInformation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public abstract class UserInformationDecorator implements UserInformationMapper {

    @Autowired
    @Qualifier("delegate")
    private UserInformationMapper delegate;

    @Override
    public ChangedUserInformationDto userInformationToUserInformationDto(UserInformation userInformation) {

        return delegate.userInformationToUserInformationDto(userInformation);
    }

    @Override
    public UserInformation userInformationDtoToUserInformation(ChangedUserInformationDto changedUserInformationDto) {
        UserInformation userInformation = delegate.userInformationDtoToUserInformation(changedUserInformationDto);

        if (changedUserInformationDto.getFirstName().isEmpty()) {
            userInformation.setFirstName(null);
        }

        if (changedUserInformationDto.getSurname().isEmpty()) {
            userInformation.setSurname(null);
        }

        if (changedUserInformationDto.getPhoneNumber().isEmpty() ||
                changedUserInformationDto.getPhoneNumber().matches("\\+375\\(__\\)___-__-__")) {
            userInformation.setPhoneNumber(null);
        }

        return userInformation;
    }
}
