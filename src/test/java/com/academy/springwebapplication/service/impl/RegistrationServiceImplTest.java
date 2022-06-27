package com.academy.springwebapplication.service.impl;

import com.academy.springwebapplication.dto.UserRegistrationDto;
import com.academy.springwebapplication.mapper.UserMapper;
import com.academy.springwebapplication.model.entity.Role;
import com.academy.springwebapplication.model.entity.User;
import com.academy.springwebapplication.model.repository.RoleRepository;
import com.academy.springwebapplication.model.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
class RegistrationServiceImplTest {

    @InjectMocks
    private RegistrationServiceImpl registrationService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserMapper userMapper;

    @Test
    void whenSaveNewUser() {
        UserRegistrationDto userRegistrationDto = new UserRegistrationDto();

        User user = new User();

        String password = "password";
        user.setPassword(password);

        when(userMapper.userRegistrationDtoToUser(userRegistrationDto)).thenReturn(user);

        String encryptedPassword = "encryptedPassword";
        when(passwordEncoder.encode(password)).thenReturn(encryptedPassword);

        Role role = new Role();
        when(roleRepository.findByName("ROLE_USER")).thenReturn(role);

        registrationService.saveNewUser(userRegistrationDto);

        verify(userMapper, times(1)).userRegistrationDtoToUser(userRegistrationDto);
        verify(passwordEncoder, times(1)).encode(password);
        verify(roleRepository, times(1)).findByName("ROLE_USER");
        verify(userRepository, times(1)).save(user);

        Assertions.assertEquals(encryptedPassword, user.getPassword());
        Assertions.assertEquals(role, user.getRole());
        Assertions.assertTrue(user.isAccountNonLocked());
    }
}
