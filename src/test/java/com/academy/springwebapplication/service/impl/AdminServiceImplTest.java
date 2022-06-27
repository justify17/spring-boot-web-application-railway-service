package com.academy.springwebapplication.service.impl;

import com.academy.springwebapplication.dto.RoleDto;
import com.academy.springwebapplication.dto.RouteDto;
import com.academy.springwebapplication.dto.TrainDto;
import com.academy.springwebapplication.dto.UserDto;
import com.academy.springwebapplication.mapper.RoleMapper;
import com.academy.springwebapplication.mapper.RouteMapper;
import com.academy.springwebapplication.mapper.TrainMapper;
import com.academy.springwebapplication.mapper.UserMapper;
import com.academy.springwebapplication.model.entity.Role;
import com.academy.springwebapplication.model.entity.Route;
import com.academy.springwebapplication.model.entity.Train;
import com.academy.springwebapplication.model.entity.User;
import com.academy.springwebapplication.model.repository.RoleRepository;
import com.academy.springwebapplication.model.repository.RouteRepository;
import com.academy.springwebapplication.model.repository.TrainRepository;
import com.academy.springwebapplication.model.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
class AdminServiceImplTest {

    @InjectMocks
    private AdminServiceImpl adminService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private RouteRepository routeRepository;

    @Mock
    private TrainRepository trainRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private RoleMapper roleMapper;

    @Mock
    private RouteMapper routeMapper;

    @Mock
    private TrainMapper trainMapper;

    @Test
    void whenGetAllUsers() {
        User firstUser = new User();
        firstUser.setUsername("admin");

        User secondUser = new User();
        secondUser.setUsername("user");

        List<User> users = List.of(firstUser, secondUser);

        when(userRepository.findAll()).thenReturn(users);

        UserDto firstUserDto = new UserDto();
        firstUserDto.setUsername("admin");

        UserDto secondUserDto = new UserDto();
        secondUserDto.setUsername("user");

        when(userMapper.userToUserDto(firstUser)).thenReturn(firstUserDto);
        when(userMapper.userToUserDto(secondUser)).thenReturn(secondUserDto);

        List<UserDto> result = List.of(firstUserDto, secondUserDto);

        assertEquals(result, adminService.getAllUsers());

        verify(userRepository, times(1)).findAll();
        verify(userMapper, times(users.size())).userToUserDto(any(User.class));
    }

    @Test
    void whenGetAllRoles() {
        Role role = new Role();
        role.setId(10);

        List<Role> roles = List.of(role);

        when(roleRepository.findAll()).thenReturn(roles);

        RoleDto roleDto = new RoleDto();
        roleDto.setId(10);

        when(roleMapper.roleToRoleDto(role)).thenReturn(roleDto);

        List<RoleDto> result = List.of(roleDto);

        assertEquals(result, adminService.getAllRoles());

        verify(roleRepository, times(1)).findAll();
        verify(roleMapper, times(roles.size())).roleToRoleDto(any(Role.class));
    }

    @Test
    void whenGetAllRoutes() {
        Route route = new Route();
        route.setType("regional");

        List<Route> routes = List.of(route);

        when(routeRepository.findAll()).thenReturn(routes);

        RouteDto routeDto = new RouteDto();
        routeDto.setType("regional");

        when(routeMapper.routeToRouteDto(route)).thenReturn(routeDto);

        List<RouteDto> result = List.of(routeDto);

        assertEquals(result, adminService.getAllRoutes());

        verify(routeRepository, times(1)).findAll();
        verify(routeMapper, times(routes.size())).routeToRouteDto(any(Route.class));
    }

    @Test
    void whenGetAllTrains() {
        Train train = new Train();
        train.setNumber("730F");

        List<Train> trains = List.of(train);

        when(trainRepository.findAll()).thenReturn(trains);

        TrainDto trainDto = new TrainDto();
        trainDto.setNumber("730F");

        when(trainMapper.trainToTrainDto(train)).thenReturn(trainDto);

        List<TrainDto> result = List.of(trainDto);

        assertEquals(result, adminService.getAllTrains());

        verify(trainRepository, times(1)).findAll();
        verify(trainMapper, times(trains.size())).trainToTrainDto(any(Train.class));
    }

    @Test
    void whenSetNewUserRole() {
        String username = "user";
        Integer newRoleId = 1;

        Role newRole = new Role();
        newRole.setId(newRoleId);

        when(roleRepository.getById(newRoleId)).thenReturn(newRole);

        Role oldRole = new Role();
        oldRole.setId(2);

        User user = new User();
        user.setUsername(username);
        user.setRole(oldRole);

        when(userRepository.findByUsername(username)).thenReturn(user);

        adminService.setNewUserRole(username, newRoleId);

        assertEquals(newRole, user.getRole());

        verify(roleRepository, times(1)).getById(newRoleId);
        verify(userRepository, times(1)).findByUsername(username);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void whenSetNewAccountStatus() {
        String username = "user";

        User user = new User();
        user.setUsername(username);
        user.setAccountNonLocked(true);

        when(userRepository.findByUsername(username)).thenReturn(user);

        boolean newAccountStatus = !user.isAccountNonLocked();

        adminService.setNewAccountStatus(username);

        assertEquals(newAccountStatus, user.isAccountNonLocked());

        verify(userRepository, times(1)).findByUsername(username);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void whenDeleteUserById() {
        Integer userId = 10;

        adminService.deleteUserById(userId);

        verify(userRepository, times(1)).deleteById(userId);
    }
}
