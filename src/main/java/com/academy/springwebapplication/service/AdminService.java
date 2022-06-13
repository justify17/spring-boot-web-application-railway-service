package com.academy.springwebapplication.service;

import com.academy.springwebapplication.dto.RoleDto;
import com.academy.springwebapplication.dto.UserDto;

import java.util.List;

public interface AdminService {
    List<UserDto> getAllUsers();

    List<RoleDto> getAllRoles();

    void setNewAccountStatus(String username);

    void setNewUserRole(String username, Integer newRoleId);
}
