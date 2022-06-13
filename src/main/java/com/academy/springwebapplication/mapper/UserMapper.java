package com.academy.springwebapplication.mapper;

import com.academy.springwebapplication.dto.UserDto;
import com.academy.springwebapplication.dto.UserRegistrationDto;
import com.academy.springwebapplication.model.entity.User;
import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

@Mapper(componentModel = "spring", uses = {RoleMapper.class})
public interface UserMapper {
    User userRegistrationDtoToUser(UserRegistrationDto userRegistrationDto);

    @Mappings({
            @Mapping(source = "user.userInformation.firstName", target = "firstName"),
            @Mapping(source = "user.userInformation.surname", target = "surname"),
            @Mapping(source = "user.userInformation.phoneNumber", target = "phoneNumber")
    })
    UserDto userToUserDto(User user);
}
