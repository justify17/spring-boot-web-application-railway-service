package com.academy.springwebapplication.service.impl;

import com.academy.springwebapplication.dto.ChangedPasswordDto;
import com.academy.springwebapplication.dto.ChangedUserInformationDto;
import com.academy.springwebapplication.dto.ChangedUsernameDto;
import com.academy.springwebapplication.mapper.UserInformationMapper;
import com.academy.springwebapplication.model.entity.User;
import com.academy.springwebapplication.model.entity.UserInformation;
import com.academy.springwebapplication.model.repository.UserInformationRepository;
import com.academy.springwebapplication.model.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
class AccountServiceImplTest {

    public static final String TEST_USERNAME = "user";

    @InjectMocks
    private AccountServiceImpl accountService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserInformationRepository userInformationRepository;

    @Mock
    private UserInformationMapper userInformationMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    private User user;

    @BeforeEach
    @Test
    void init() {
        user = new User();
        user.setId(1);
        user.setUsername(TEST_USERNAME);
        user.setPassword("password");
        user.setUserInformation(new UserInformation());
    }

    @Test
    void whenGetUserInformation_AndUserInformationIsNotNull() {
        when(userRepository.findByUsername(TEST_USERNAME)).thenReturn(user);

        ChangedUserInformationDto result = new ChangedUserInformationDto();
        when(userInformationMapper.userInformationToUserInformationDto(user.getUserInformation())).thenReturn(result);

        assertEquals(result, accountService.getUserInformation(TEST_USERNAME));
        verify(userRepository, times(1)).findByUsername(TEST_USERNAME);
        verify(userInformationMapper, times(1)).userInformationToUserInformationDto(user.getUserInformation());
    }

    @Test
    void whenGetUserInformation_AndUserInformationIsNull() {
        user.setUserInformation(null);

        when(userRepository.findByUsername(TEST_USERNAME)).thenReturn(user);

        ChangedUserInformationDto result = new ChangedUserInformationDto();
        result.setUsername(TEST_USERNAME);

        assertEquals(result, accountService.getUserInformation(TEST_USERNAME));

        verify(userRepository, times(1)).findByUsername(TEST_USERNAME);
        verify(userInformationMapper, never()).userInformationToUserInformationDto(user.getUserInformation());
    }

    @Test
    void whenSaveNewUserInformation_AndThereIsAnUpdate() {
        ChangedUserInformationDto newUserInformationDto = new ChangedUserInformationDto();
        newUserInformationDto.setUsername(TEST_USERNAME);
        newUserInformationDto.setSurname("Surname");

        when(userRepository.findByUsername(newUserInformationDto.getUsername())).thenReturn(user);

        UserInformation newUserInformation = new UserInformation();
        when(userInformationMapper.userInformationDtoToUserInformation(newUserInformationDto)).thenReturn(newUserInformation);

        accountService.saveNewUserInformation(newUserInformationDto);

        assertNotNull(newUserInformation.getId());
        assertEquals(user, newUserInformation.getUser());

        verify(userRepository, times(1)).findByUsername(newUserInformationDto.getUsername());
        verify(userInformationMapper, times(1)).userInformationDtoToUserInformation(newUserInformationDto);
        verify(userInformationRepository, times(1)).save(newUserInformation);
    }

    @Test
    void whenSaveNewUserInformation_AndThereIsAnSave() {
        ChangedUserInformationDto newUserInformationDto = new ChangedUserInformationDto();
        newUserInformationDto.setUsername(TEST_USERNAME);
        newUserInformationDto.setSurname("Surname");

        user.setUserInformation(null);
        when(userRepository.findByUsername(newUserInformationDto.getUsername())).thenReturn(user);

        UserInformation newUserInformation = new UserInformation();
        when(userInformationMapper.userInformationDtoToUserInformation(newUserInformationDto)).thenReturn(newUserInformation);

        accountService.saveNewUserInformation(newUserInformationDto);

        assertNull(newUserInformation.getId());
        assertEquals(user, newUserInformation.getUser());

        verify(userRepository, times(1)).findByUsername(newUserInformationDto.getUsername());
        verify(userInformationMapper, times(1)).userInformationDtoToUserInformation(newUserInformationDto);
        verify(userInformationRepository, times(1)).save(newUserInformation);
    }

    @Test
    void whenSaveNewUsername() {
        ChangedUsernameDto changedUsernameDto = new ChangedUsernameDto();
        changedUsernameDto.setUsername(TEST_USERNAME);
        changedUsernameDto.setNewUsername("NewUsername");

        when(userRepository.findByUsername(changedUsernameDto.getUsername())).thenReturn(user);

        accountService.saveNewUsername(changedUsernameDto);

        assertEquals(changedUsernameDto.getNewUsername(), user.getUsername());

        verify(userRepository,times(1)).findByUsername(changedUsernameDto.getUsername());
        verify(userRepository,times(1)).save(user);
    }

    @Test
    void whenSaveNewPassword(){
        ChangedPasswordDto changedPasswordDto = new ChangedPasswordDto();
        changedPasswordDto.setUsername(TEST_USERNAME);
        changedPasswordDto.setNewPassword("newPassword");

        when(userRepository.findByUsername(changedPasswordDto.getUsername())).thenReturn(user);

        String encryptedPassword = "encryptedNewPassword";
        when(passwordEncoder.encode(changedPasswordDto.getNewPassword())).thenReturn(encryptedPassword);

        accountService.saveNewPassword(changedPasswordDto);

        assertEquals(encryptedPassword,user.getPassword());

        verify(userRepository,times(1)).findByUsername(changedPasswordDto.getUsername());
        verify(passwordEncoder,times(1)).encode(changedPasswordDto.getNewPassword());
        verify(userRepository,times(1)).save(user);
    }
}
