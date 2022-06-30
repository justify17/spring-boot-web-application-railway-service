package com.academy.springwebapplication.controller;

import com.academy.springwebapplication.dto.*;
import com.academy.springwebapplication.model.entity.Departure;
import com.academy.springwebapplication.service.DepartureService;
import com.academy.springwebapplication.service.OrderService;
import com.academy.springwebapplication.service.StationService;
import com.academy.springwebapplication.service.TicketGenerationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class OrderController {
    private final DepartureService departureService;
    private final StationService stationService;
    private final TicketGenerationService ticketGenerationService;
    private final OrderService orderService;

    @GetMapping("/order")
    public String order(@RequestParam(name = "departureId") int departureId,
                        @RequestParam(name = "departureStation") String departureStation,
                        @RequestParam(name = "arrivalStation") String arrivalStation,
                        Model model, HttpSession session) {
        departureService.checkIfDepartureIdIsValid(departureId);
        stationService.checkIfStationTitleIsValid(departureStation);
        stationService.checkIfStationTitleIsValid(arrivalStation);

        Departure departure = departureService.getDepartureById(departureId);
        UserRouteDto userRouteDto = UserRouteDto.builder()
                .departureStation(StationDto.builder().title(departureStation).build())
                .arrivalStation(StationDto.builder().title(arrivalStation).build())
                .build();

        TicketDto ticket = ticketGenerationService.createTicketForDepartureAlongRoute(departure, userRouteDto);

        setModelData(model, ticket);

        session.setAttribute("ticket", ticket);

        return "order";
    }

    @PostMapping(value = "/order", params = {"hiddenAction=carriage"})
    public String chooseTicketCarriage(@RequestParam(value = "carriageNumber") int carriageNumber,
                                       @SessionAttribute("ticket") TicketDto ticket,
                                       Model model) {
        ticket.setCarriageNumber(carriageNumber);
        ticket.setSeatNumber(null);

        orderService.setCarriageComfortLevelForTicket(ticket);

        setModelData(model, ticket);

        return "order";
    }

    @PostMapping(value = "/order", params = {"hiddenAction=seat"})
    public String chooseTicketSeat(@RequestParam(value = "seatNumber") int seatNumber,
                                   @SessionAttribute("ticket") TicketDto ticket,
                                   Model model) {
        ticket.setSeatNumber(seatNumber);

        orderService.setTicketFinalPrice(ticket);

        setModelData(model, ticket);

        return "order";
    }

    @PostMapping(value = "/order", params = {"hiddenAction=payment"})
    public String ticketPayment(@Valid @ModelAttribute("card") CreditCardDto card,
                                BindingResult bindingResult, Model model,
                                @SessionAttribute("ticket") TicketDto ticket,
                                @AuthenticationPrincipal UserDetails userDetails,
                                HttpSession session) {
        if (bindingResult.hasErrors()) {
            setModelData(model, ticket);

            model.addAttribute("openPayment", true);

            return "order";
        }

        ticket.setUsername(userDetails.getUsername());

        try {
            orderService.payTicket(card, ticket);
        } catch (Exception e) {
            setModelData(model, ticket);

            bindingResult.reject("operationError", "Operation failed, please try again");

            return "order";
        }

        model.addAttribute("ticket", ticket);

        session.removeAttribute("ticket");

        return "successfulOrder";
    }

    private void setModelData(Model model, TicketDto ticket) {
        if (!model.containsAttribute("card")) {

            model.addAttribute("card", new CreditCardDto());
        }

        if (ticket.getCarriageNumber() != null) {
            List<SeatDto> seats = orderService.getSeatsByTicketData(ticket);

            model.addAttribute("seats", seats);
        }
    }
}
