package com.academy.springwebapplication.mapper;

import com.academy.springwebapplication.dto.UserDto;
import com.academy.springwebapplication.model.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User userDtoToUser(UserDto userDto);
}
