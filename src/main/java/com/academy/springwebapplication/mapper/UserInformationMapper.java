package com.academy.springwebapplication.mapper;

import com.academy.springwebapplication.dto.UserInformationDto;
import com.academy.springwebapplication.model.entity.UserInformation;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserInformationMapper {
    UserInformationDto userInformationToUserInformationDto(UserInformation userInformation);

    UserInformation userInformationDtoToUserInformation(UserInformationDto userInformationDto);
}
