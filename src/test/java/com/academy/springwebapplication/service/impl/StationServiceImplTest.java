package com.academy.springwebapplication.service.impl;

import com.academy.springwebapplication.exception.EntityByTitleNotFoundException;
import com.academy.springwebapplication.model.entity.Station;
import com.academy.springwebapplication.model.repository.StationRepository;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
class StationServiceImplTest {

    @InjectMocks
    private StationServiceImpl stationService;

    @Mock
    private StationRepository stationRepository;

    @Test
    void whenCheckIfStationTitleIsValid_AndStationIsNotExisting() {
        String stationTitle = "notExistingStation";

        when(stationRepository.findByTitle(stationTitle)).thenReturn(null);

        assertThrows(EntityByTitleNotFoundException.class, () -> stationService.checkIfStationTitleIsValid(stationTitle));

        verify(stationRepository, times(1)).findByTitle(stationTitle);
    }

    @Test
    void whenCheckIfStationTitleIsValid_AndStationIsExisting() {
        String stationTitle = "Гомель";

        when(stationRepository.findByTitle(stationTitle)).thenReturn(new Station());

        stationService.checkIfStationTitleIsValid(stationTitle);

        verify(stationRepository, times(1)).findByTitle(stationTitle);
    }
}
