package com.academy.springwebapplication.validator;

import com.academy.springwebapplication.model.entity.User;
import com.academy.springwebapplication.model.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import javax.validation.ConstraintValidatorContext;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
class UsernameValidatorTest {

    @InjectMocks
    private UsernameValidator usernameValidator;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ConstraintValidatorContext constraintValidatorContext;

    @Test
    void testWhenUsernameIsNotValid() {
        String newUsername = "busyUsername";

        User user = new User();
        user.setId(1);

        when(userRepository.findByUsername(newUsername)).thenReturn(user);

        assertFalse(usernameValidator.isValid(newUsername, constraintValidatorContext));
    }

    @Test
    void testWhenUsernameIsValid() {
        String newUsername = "notTakenUsername";

        when(userRepository.findByUsername(newUsername)).thenReturn(null);

        assertTrue(usernameValidator.isValid(newUsername, constraintValidatorContext));
    }
}
