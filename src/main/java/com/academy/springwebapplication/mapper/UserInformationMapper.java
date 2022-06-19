package com.academy.springwebapplication.mapper;

import com.academy.springwebapplication.dto.ChangedUserInformationDto;
import com.academy.springwebapplication.mapper.decorator.UserInformationDecorator;
import com.academy.springwebapplication.model.entity.UserInformation;
import org.mapstruct.DecoratedWith;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
@DecoratedWith(UserInformationDecorator.class)
public interface UserInformationMapper {

    @Mapping(source = "userInformation.user.username", target = "username")
    ChangedUserInformationDto userInformationToUserInformationDto(UserInformation userInformation);

    @InheritInverseConfiguration
    UserInformation userInformationDtoToUserInformation(ChangedUserInformationDto changedUserInformationDto);
}
