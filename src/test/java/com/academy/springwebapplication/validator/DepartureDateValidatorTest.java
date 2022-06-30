package com.academy.springwebapplication.validator;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
class DepartureDateValidatorTest {

    @InjectMocks
    private DepartureDateValidator departureDateValidator;

    @Mock
    private ConstraintValidatorContext constraintValidatorContext;

    @Test
    void testWhenDateIsNull() {
        LocalDate localDate = null;

        assertTrue(departureDateValidator.isValid(localDate, constraintValidatorContext));
    }

    @Test
    void testWhenDateIsNotValid() {
        LocalDate localDate = LocalDate.now().plusMonths(5);

        assertFalse(departureDateValidator.isValid(localDate, constraintValidatorContext));
    }

    @Test
    void testWhenDateIsValid() {
        LocalDate localDate = LocalDate.now().plusMonths(1);

        assertTrue(departureDateValidator.isValid(localDate, constraintValidatorContext));
    }
}
