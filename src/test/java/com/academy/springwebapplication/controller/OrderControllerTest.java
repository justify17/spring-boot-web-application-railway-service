package com.academy.springwebapplication.controller;

import com.academy.springwebapplication.dto.CreditCardDto;
import com.academy.springwebapplication.dto.StationDto;
import com.academy.springwebapplication.dto.TicketDto;
import com.academy.springwebapplication.dto.UserRouteDto;
import com.academy.springwebapplication.exception.EntityByIdNotFoundException;
import com.academy.springwebapplication.exception.FailedPaymentException;
import com.academy.springwebapplication.model.entity.Departure;
import com.academy.springwebapplication.service.*;
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

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @SpyBean
    private TestObjectFactory testObjectFactory;

    @MockBean
    private DepartureService departureService;

    @MockBean
    private TicketGenerationService ticketGenerationService;

    @MockBean
    private OrderService orderService;

    @MockBean
    private StationService stationService;

    @Test
    void whenOrder_AndRequestParamsAreValid() throws Exception {
        Integer validDepartureId = 1;
        String validDepartureStation = "Гомель";
        String validArrivalStation = "Минск";

        UserRouteDto userRouteDto = UserRouteDto.builder()
                .departureStation(StationDto.builder().title(validDepartureStation).build())
                .arrivalStation(StationDto.builder().title(validArrivalStation).build())
                .build();

        Departure departure = new Departure();

        when(departureService.getDepartureById(validDepartureId)).thenReturn(departure);

        TicketDto ticketDto = testObjectFactory.getTicketDto();
        ticketDto.getDeparture().getRoute().setType("Региональные линии");

        when(ticketGenerationService.createTicketForDepartureAlongRoute(departure, userRouteDto)).thenReturn(ticketDto);

        this.mockMvc.perform(get("/order")
                        .param("departureId", String.valueOf(validDepartureId))
                        .param("departureStation", validDepartureStation)
                        .param("arrivalStation", validArrivalStation))
                .andExpect(model().attributeExists("card"))
                .andExpect(model().attributeDoesNotExist("seats"))
                .andExpect(status().isOk())
                .andExpect(view().name("order"));

        verify(departureService, times(1)).checkIfDepartureIdIsValid(validDepartureId);
        verify(stationService, times(1)).checkIfStationTitleIsValid(validDepartureStation);
        verify(stationService, times(1)).checkIfStationTitleIsValid(validArrivalStation);
        verify(departureService, times(1)).getDepartureById(validDepartureId);
        verify(ticketGenerationService, times(1)).createTicketForDepartureAlongRoute(departure, userRouteDto);

        verifyNoMoreInteractions(departureService);
        verifyNoMoreInteractions(ticketGenerationService);
        verifyNoMoreInteractions(stationService);
    }

    @Test
    void whenOrder_AndAnyRequestParamIsNotValid() throws Exception {
        Integer invalidDepartureId = -25;
        String validDepartureStation = "Гомель";
        String validArrivalStation = "Минск";

        doThrow(new EntityByIdNotFoundException(invalidDepartureId))
                .when(departureService)
                .checkIfDepartureIdIsValid(invalidDepartureId);

        this.mockMvc.perform(get("/order")
                        .param("departureId", String.valueOf(invalidDepartureId))
                        .param("departureStation", validDepartureStation)
                        .param("arrivalStation", validArrivalStation))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertTrue(result.getResolvedException() instanceof EntityByIdNotFoundException));

        verify(departureService, times(1)).checkIfDepartureIdIsValid(invalidDepartureId);
        verify(stationService, never()).checkIfStationTitleIsValid(validDepartureStation);
        verify(stationService, never()).checkIfStationTitleIsValid(validArrivalStation);
        verify(departureService, never()).getDepartureById(invalidDepartureId);
        verify(ticketGenerationService, never()).createTicketForDepartureAlongRoute(any(Departure.class), any(UserRouteDto.class));

        verifyNoMoreInteractions(departureService);
        verifyNoMoreInteractions(ticketGenerationService);
        verifyNoMoreInteractions(stationService);
    }

    @Test
    void whenChooseTicketCarriage() throws Exception {
        Integer carriageNumber = 1;

        TicketDto ticketDto = testObjectFactory.getTicketDto();
        ticketDto.setCarriageNumber(carriageNumber);

        this.mockMvc.perform(post("/order")
                        .param("hiddenAction", "carriage")
                        .param("carriageNumber", String.valueOf(carriageNumber))
                        .sessionAttr("ticket", ticketDto))
                .andExpect(model().attributeExists("card"))
                .andExpect(model().attributeExists("seats"))
                .andExpect(status().isOk())
                .andExpect(view().name("order"));

        verify(orderService, times(1)).setCarriageComfortLevelForTicket(ticketDto);
        verify(orderService, times(1)).getSeatsByTicketData(ticketDto);

        verifyNoMoreInteractions(orderService);
    }

    @Test
    void whenChooseTicketSeat() throws Exception {
        Integer carriageNumber = 1;
        Integer seatNumber = 1;

        TicketDto ticketDto = testObjectFactory.getTicketDto();
        ticketDto.setCarriageNumber(carriageNumber);
        ticketDto.setSeatNumber(seatNumber);

        this.mockMvc.perform(post("/order")
                        .param("hiddenAction", "seat")
                        .param("seatNumber", String.valueOf(seatNumber))
                        .sessionAttr("ticket", ticketDto))
                .andExpect(model().attributeExists("card"))
                .andExpect(model().attributeExists("seats"))
                .andExpect(status().isOk())
                .andExpect(view().name("order"));

        verify(orderService, times(1)).setTicketFinalPrice(ticketDto);
        verify(orderService, times(1)).getSeatsByTicketData(ticketDto);

        verifyNoMoreInteractions(orderService);
    }

    @Test
    void whenTicketPayment_AndModelIsInvalid() throws Exception {
        Integer carriageNumber = 1;
        Integer seatNumber = 1;

        TicketDto ticketDto = testObjectFactory.getTicketDto();
        ticketDto.setCarriageNumber(carriageNumber);
        ticketDto.setSeatNumber(seatNumber);
        ticketDto.setAdditionalPrice(500);
        ticketDto.setFinalPrice(2000.0);

        CreditCardDto invalidCreditCard = testObjectFactory.getInvalidCreditCardDto();

        this.mockMvc.perform(post("/order")
                        .param("hiddenAction", "payment")
                        .param("number", invalidCreditCard.getNumber())
                        .param("monthOfCardExpiration", String.valueOf(invalidCreditCard.getMonthOfCardExpiration()))
                        .param("yearOfCardExpiration", String.valueOf(invalidCreditCard.getYearOfCardExpiration()))
                        .sessionAttr("ticket", ticketDto))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrors("card",
                        "number", "monthOfCardExpiration", "yearOfCardExpiration"))
                .andExpect(model().attributeExists("seats"))
                .andExpect(model().attributeExists("openPayment"))
                .andExpect(status().isOk())
                .andExpect(view().name("order"));

        verify(orderService, never()).payTicket(invalidCreditCard, ticketDto);
        verify(orderService, times(1)).getSeatsByTicketData(ticketDto);

        verifyNoMoreInteractions(orderService);
    }

    @Test
    void whenTicketPayment_AndModelIsValid_AndSuccessfulPayment() throws Exception {
        Integer carriageNumber = 1;
        Integer seatNumber = 1;

        TicketDto ticketDto = testObjectFactory.getTicketDto();
        ticketDto.setCarriageNumber(carriageNumber);
        ticketDto.setSeatNumber(seatNumber);
        ticketDto.setAdditionalPrice(500);
        ticketDto.setFinalPrice(2000.0);

        CreditCardDto creditCard = testObjectFactory.getValidCreditCardDto();

        this.mockMvc.perform(post("/order")
                        .param("hiddenAction", "payment")
                        .param("number", creditCard.getNumber())
                        .param("monthOfCardExpiration", String.valueOf(creditCard.getMonthOfCardExpiration()))
                        .param("yearOfCardExpiration", String.valueOf(creditCard.getYearOfCardExpiration()))
                        .sessionAttr("ticket", ticketDto))
                .andExpect(model().hasNoErrors())
                .andExpect(model().attributeExists("ticket"))
                .andExpect(status().isOk())
                .andExpect(view().name("successfulOrder"));

        verify(orderService, times(1)).payTicket(creditCard, ticketDto);

        verifyNoMoreInteractions(orderService);
    }

    @Test
    void whenTicketPayment_AndModelIsValid_AndFailedPayment() throws Exception {
        Integer carriageNumber = 1;
        Integer seatNumber = 1;

        TicketDto ticketDto = testObjectFactory.getTicketDto();
        ticketDto.setCarriageNumber(carriageNumber);
        ticketDto.setSeatNumber(seatNumber);
        ticketDto.setAdditionalPrice(500);
        ticketDto.setFinalPrice(2000.0);

        CreditCardDto creditCard = testObjectFactory.getValidCreditCardDto();

        doThrow(new FailedPaymentException(creditCard.getNumber()))
                .when(orderService)
                .payTicket(creditCard, ticketDto);

        this.mockMvc.perform(post("/order")
                        .param("hiddenAction", "payment")
                        .param("number", creditCard.getNumber())
                        .param("monthOfCardExpiration", String.valueOf(creditCard.getMonthOfCardExpiration()))
                        .param("yearOfCardExpiration", String.valueOf(creditCard.getYearOfCardExpiration()))
                        .sessionAttr("ticket", ticketDto))
                .andExpect(model().errorCount(1))
                .andExpect(model().attributeHasErrors("card"))
                .andExpect(status().isOk())
                .andExpect(view().name("order"));

        verify(orderService, times(1)).payTicket(creditCard, ticketDto);
        verify(orderService, times(1)).getSeatsByTicketData(ticketDto);

        verifyNoMoreInteractions(orderService);
    }
}
