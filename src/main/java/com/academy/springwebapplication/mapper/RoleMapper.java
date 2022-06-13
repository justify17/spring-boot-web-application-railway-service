package com.academy.springwebapplication.mapper;

import com.academy.springwebapplication.dto.RoleDto;
import com.academy.springwebapplication.model.entity.Role;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    default RoleDto roleToRoleDto(Role role){
        RoleDto roleDto = new RoleDto();

        roleDto.setId(role.getId());

        switch (role.getName()) {
            case "ROLE_ADMIN":
                roleDto.setName("ADMIN");
                break;
            case "ROLE_USER":
                roleDto.setName("USER");
                break;
            default:
                roleDto.setName("-");
        }

        return roleDto;
    }
}
