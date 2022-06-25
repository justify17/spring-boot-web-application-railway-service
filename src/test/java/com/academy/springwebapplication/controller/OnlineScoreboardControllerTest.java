package com.academy.springwebapplication.controller;

import com.academy.springwebapplication.dto.DepartureDto;
import com.academy.springwebapplication.dto.StationDto;
import com.academy.springwebapplication.service.DepartureService;
import com.academy.springwebapplication.util.TestObjectFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser
class OnlineScoreboardControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @SpyBean
    private TestObjectFactory testObjectFactory;

    @MockBean
    private DepartureService departureService;

    @Test
    void whenOnlineScoreboard() throws Exception {
        this.mockMvc.perform(get("/onlineScoreboard"))
                .andExpect(model().attributeExists("station"))
                .andExpect(status().isOk())
                .andExpect(view().name("onlineScoreboard"));
    }

    @Test
    void whenFindingDeparturesByStation_AndModelIsValid() throws Exception {
        StationDto station = testObjectFactory.getValidStationDto();
        List<DepartureDto> departures = List.of(testObjectFactory.getDepartureDto());

        when(departureService.getDeparturesByStation(station)).thenReturn(departures);

        this.mockMvc.perform(post("/onlineScoreboard")
                        .param("title", station.getTitle()))
                .andExpect(model().hasNoErrors())
                .andExpect(model().attributeExists("departures"))
                .andExpect(status().isOk())
                .andExpect(view().name("onlineScoreboard"));

        verify(departureService, times(1)).getDeparturesByStation(station);
        verifyNoMoreInteractions(departureService);
    }

    @Test
    void whenFindingDeparturesByStation_AndModelIsInvalid() throws Exception {
        StationDto station = testObjectFactory.getInvalidStationDto();

        this.mockMvc.perform(post("/onlineScoreboard")
                        .param("title", station.getTitle()))
                .andExpect(model().attributeHasFieldErrors("station", "title"))
                .andExpect(model().attributeDoesNotExist("departures"))
                .andExpect(status().isOk())
                .andExpect(view().name("onlineScoreboard"));

        verify(departureService, never()).getDeparturesByStation(station);
    }
}
