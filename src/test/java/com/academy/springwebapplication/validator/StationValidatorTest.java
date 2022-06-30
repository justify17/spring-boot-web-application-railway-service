package com.academy.springwebapplication.validator;

import com.academy.springwebapplication.model.entity.Station;
import com.academy.springwebapplication.model.repository.StationRepository;
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
class StationValidatorTest {

    @InjectMocks
    private StationValidator stationValidator;

    @Mock
    private StationRepository stationRepository;

    @Mock
    private ConstraintValidatorContext constraintValidatorContext;

    @Test
    void testWhenStationTitleIsEmpty() {
        String stationTitle = "";

        assertTrue(stationValidator.isValid(stationTitle, constraintValidatorContext));
    }

    @Test
    void testWhenStationTitleIsNotValid() {
        String stationTitle = "Москва123";

        when(stationRepository.findByTitle(stationTitle)).thenReturn(null);

        assertFalse(stationValidator.isValid(stationTitle, constraintValidatorContext));
    }

    @Test
    void testWhenStationTitleIsValid() {
        String stationTitle = "Гомель";

        Station station = new Station();
        station.setId(1);
        station.setTitle(stationTitle);

        when(stationRepository.findByTitle(stationTitle)).thenReturn(station);

        assertTrue(stationValidator.isValid(stationTitle, constraintValidatorContext));
    }
}
