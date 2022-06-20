package com.academy.springwebapplication.controller;

import com.academy.springwebapplication.dto.*;
import com.academy.springwebapplication.model.entity.Departure;
import com.academy.springwebapplication.service.DepartureService;
import com.academy.springwebapplication.service.TicketService;
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
    private final TicketService ticketService;

    @GetMapping("/order")
    public String order(@RequestParam(name = "idDeparture") int idDeparture,
                        @RequestParam(name = "departureStation") String departureStation,
                        @RequestParam(name = "arrivalStation") String arrivalStation,
                        Model model, HttpSession session) {
        Departure departure = departureService.getDepartureById(idDeparture);
        UserRouteDto userRouteDto = new UserRouteDto(new StationDto(departureStation), new StationDto(arrivalStation));

        TicketDto ticket = ticketService.createTicketForDepartureAlongRoute(departure, userRouteDto);

        if (ticket.getDeparture().getRoute().getType().equals("Региональные линии")) {
            ticketService.setTicketFinalPrice(ticket);
        }

        setModelData(model, ticket);

        session.setAttribute("ticket", ticket);

        return "order";
    }

    @PostMapping(value = "/order", params = {"hiddenAction=carriage"})
    public String ticketCarriage(@RequestParam(value = "carriageNumber") int carriageNumber,
                                 @SessionAttribute("ticket") TicketDto ticket,
                                 Model model) {
        ticket.setCarriageNumber(carriageNumber);
        ticket.setSeatNumber(null);

        ticketService.setCarriageComfortLevelForTicket(ticket);

        setModelData(model, ticket);

        return "order";
    }

    @PostMapping(value = "/order", params = {"hiddenAction=seat"})
    public String ticketSeat(@RequestParam(value = "seatNumber") int seatNumber,
                             @SessionAttribute("ticket") TicketDto ticket,
                             Model model) {
        ticket.setSeatNumber(seatNumber);

        ticketService.setTicketFinalPrice(ticket);

        setModelData(model, ticket);

        return "order";
    }

    @PostMapping(value = "/order", params = {"hiddenAction=payment"})
    public String ticketPayment(@Valid @ModelAttribute("card") CreditCardDto card,
                                BindingResult bindingResult, Model model,
                                @SessionAttribute("ticket") TicketDto ticket,
                                @AuthenticationPrincipal UserDetails userDetails,
                                HttpSession session) {
        if(bindingResult.hasErrors()){
            setModelData(model, ticket);

            model.addAttribute("openPayment", true);

            return "order";
        }

        ticket.setUsername(userDetails.getUsername());

        try {
            ticketService.payTicket(card, ticket);
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
        if(!model.containsAttribute("card")){

            model.addAttribute("card", new CreditCardDto());
        }

        if (ticket.getCarriageNumber() != null) {
            List<SeatDto> seats = ticketService.getSeatsByTicketData(ticket);

            model.addAttribute("seats", seats);
        }
    }
}
