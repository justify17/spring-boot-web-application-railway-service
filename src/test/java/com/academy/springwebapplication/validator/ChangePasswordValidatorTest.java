package com.academy.springwebapplication.validator;

import com.academy.springwebapplication.dto.ChangedPasswordDto;
import com.academy.springwebapplication.model.entity.User;
import com.academy.springwebapplication.model.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.ConstraintValidatorContext;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
class ChangePasswordValidatorTest {

    @InjectMocks
    private ChangePasswordValidator changePasswordValidator;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private ConstraintValidatorContext constraintValidatorContext;

    @Mock
    private ConstraintValidatorContext.ConstraintViolationBuilder constraintViolationBuilder;

    @Mock
    private ConstraintValidatorContext.ConstraintViolationBuilder.NodeBuilderCustomizableContext nodeBuilderCustomizableContext;

    @BeforeEach
    void init() {
        when(constraintValidatorContext.buildConstraintViolationWithTemplate(any())).thenReturn(constraintViolationBuilder);
        when(constraintViolationBuilder.addPropertyNode(any())).thenReturn(nodeBuilderCustomizableContext);
        when(constraintViolationBuilder.addConstraintViolation()).thenReturn(constraintValidatorContext);
    }

    @Test
    void testWhenOldPasswordIsNotValid() {
        ChangedPasswordDto changedPasswordDto = new ChangedPasswordDto();
        changedPasswordDto.setUsername("user");
        changedPasswordDto.setPassword("invalidOldPassword");
        changedPasswordDto.setNewPassword("newPassword");
        changedPasswordDto.setConfirmNewPassword("newPassword");

        User user = new User();
        user.setPassword("oldPassword");

        when(userRepository.findByUsername(changedPasswordDto.getUsername())).thenReturn(user);
        when(passwordEncoder.matches(changedPasswordDto.getPassword(), user.getPassword())).thenReturn(false);

        assertFalse(changePasswordValidator.isValid(changedPasswordDto, constraintValidatorContext));
    }

    @Test
    void testWhenNewPasswordsAreNotValid() {
        ChangedPasswordDto changedPasswordDto = new ChangedPasswordDto();
        changedPasswordDto.setUsername("user");
        changedPasswordDto.setPassword("oldPassword");
        changedPasswordDto.setNewPassword("newPassword");
        changedPasswordDto.setConfirmNewPassword("newPassword123");

        User user = new User();
        user.setPassword("oldPassword");

        when(userRepository.findByUsername(changedPasswordDto.getUsername())).thenReturn(user);
        when(passwordEncoder.matches(changedPasswordDto.getPassword(), user.getPassword())).thenReturn(true);

        assertFalse(changePasswordValidator.isValid(changedPasswordDto, constraintValidatorContext));
    }

    @Test
    void testWhenDtoIsValid() {
        ChangedPasswordDto changedPasswordDto = new ChangedPasswordDto();
        changedPasswordDto.setUsername("user");
        changedPasswordDto.setPassword("oldPassword");
        changedPasswordDto.setNewPassword("newPassword123");
        changedPasswordDto.setConfirmNewPassword("newPassword123");

        User user = new User();
        user.setPassword("oldPassword");

        when(userRepository.findByUsername(changedPasswordDto.getUsername())).thenReturn(user);
        when(passwordEncoder.matches(changedPasswordDto.getPassword(), user.getPassword())).thenReturn(true);

        assertTrue(changePasswordValidator.isValid(changedPasswordDto, constraintValidatorContext));
    }
}
