package com.academy.springwebapplication.mapper;

import com.academy.springwebapplication.dto.UserInformationDto;
import com.academy.springwebapplication.model.entity.UserInformation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserInformationMapper {

    @Mapping(source = "userInformation.user.username", target = "username")
    UserInformationDto userInformationToUserInformationDto(UserInformation userInformation);

    UserInformation userInformationDtoToUserInformation(UserInformationDto userInformationDto);
}
