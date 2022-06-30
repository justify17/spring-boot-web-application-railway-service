package com.academy.springwebapplication.controller;

import com.academy.springwebapplication.dto.DepartureDto;
import com.academy.springwebapplication.dto.TicketDto;
import com.academy.springwebapplication.dto.UserRouteDto;
import com.academy.springwebapplication.exception.EntityByIdNotFoundException;
import com.academy.springwebapplication.mapper.DepartureMapper;
import com.academy.springwebapplication.model.entity.Departure;
import com.academy.springwebapplication.service.DepartureService;
import com.academy.springwebapplication.service.TicketGenerationService;
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

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser
class DepartureControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @SpyBean
    private TestObjectFactory testObjectFactory;

    @MockBean
    private DepartureService departureService;

    @MockBean
    private DepartureMapper departureMapper;

    @MockBean
    private TicketGenerationService ticketGenerationService;

    @Test
    void whenDepartures() throws Exception {
        this.mockMvc.perform(get("/departures"))
                .andExpect(model().attributeExists("userRoute"))
                .andExpect(status().isOk())
                .andExpect(view().name("departures"));
    }

    @Test
    void whenFindingDeparturesForRoute_AndModelIsValid() throws Exception {
        UserRouteDto userRoute = testObjectFactory.getValidUserRouteDto();
        List<Departure> departures = List.of(new Departure());
        List<TicketDto> tickets = List.of(testObjectFactory.getTicketDto());

        when(departureService.getDeparturesForRoute(userRoute)).thenReturn(departures);
        when(ticketGenerationService.generateTicketsSuitableForUserRoute(departures, userRoute)).thenReturn(tickets);

        this.mockMvc.perform(post("/departures")
                        .param("departureStation.title", userRoute.getDepartureStation().getTitle())
                        .param("arrivalStation.title", userRoute.getArrivalStation().getTitle())
                        .param("departureDate", userRoute.getDepartureDate().toString())
                        .param("departureTime", userRoute.getDepartureTime().toString()))
                .andExpect(model().hasNoErrors())
                .andExpect(model().attributeExists("tickets"))
                .andExpect(status().isOk())
                .andExpect(view().name("departures"));

        verify(departureService, times(1)).getDeparturesForRoute(userRoute);
        verify(ticketGenerationService, times(1)).generateTicketsSuitableForUserRoute(departures, userRoute);
        verifyNoMoreInteractions(departureService);
        verifyNoMoreInteractions(ticketGenerationService);
    }

    @Test
    void whenFindingDeparturesForRoute_AndModelIsInvalid() throws Exception {
        UserRouteDto userRoute = testObjectFactory.getInvalidUserRouteDto();

        this.mockMvc.perform(post("/departures")
                        .param("departureStation.title", userRoute.getDepartureStation().getTitle())
                        .param("arrivalStation.title", userRoute.getArrivalStation().getTitle())
                        .param("departureDate", userRoute.getDepartureDate().toString())
                        .param("departureTime", userRoute.getDepartureTime().toString()))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("userRoute",
                        "departureStation.title", "arrivalStation.title", "departureDate"))
                .andExpect(model().attributeDoesNotExist("tickets"))
                .andExpect(status().isOk())
                .andExpect(view().name("departures"));

        verify(departureService, never()).getDeparturesForRoute(userRoute);
        verify(ticketGenerationService, never()).generateTicketsSuitableForUserRoute(anyList(), eq(userRoute));
    }

    @Test
    void whenDepartureRoute_AndRequestParamIsValid() throws Exception {
        Integer departureId = 1;
        Departure departure = new Departure();
        DepartureDto departureDto = testObjectFactory.getDepartureDto();

        doNothing().when(departureService).checkIfDepartureIdIsValid(departureId);

        when(departureService.getDepartureById(departureId)).thenReturn(departure);
        when(departureMapper.departureToDepartureDto(departure)).thenReturn(departureDto);

        this.mockMvc.perform(get("/departures/route")
                        .param("departureId", String.valueOf(departureId)))
                .andExpect(model().attributeExists("departure"))
                .andExpect(status().isOk())
                .andExpect(view().name("departureRoute"));

        verify(departureService, times(1)).checkIfDepartureIdIsValid(departureId);
        verify(departureService, times(1)).getDepartureById(departureId);
        verify(departureMapper, times(1)).departureToDepartureDto(departure);

        verifyNoMoreInteractions(departureService);
        verifyNoMoreInteractions(departureMapper);
    }

    @Test
    void whenDepartureRoute_AndRequestParamIsInvalid() throws Exception {
        Integer departureId = 0;

        doThrow(new EntityByIdNotFoundException(departureId))
                .when(departureService)
                .checkIfDepartureIdIsValid(departureId);

        this.mockMvc.perform(get("/departures/route")
                        .param("departureId", String.valueOf(departureId)))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof EntityByIdNotFoundException));

        verify(departureService, times(1)).checkIfDepartureIdIsValid(departureId);
        verify(departureService, never()).getDepartureById(departureId);
        verify(departureMapper, never()).departureToDepartureDto(any(Departure.class));
    }
}
