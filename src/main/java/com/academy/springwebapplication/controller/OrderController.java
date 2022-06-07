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
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
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

        TicketDto ticket = ticketService.getTicketForDepartureAlongTheRoute(departure, userRouteDto);

        if (ticket.getDeparture().getRoute().getType().equals("Региональные линии")) {
            ticketService.setTicketFinalPrice(ticket);
        }

        setModelData(model, ticket);

        session.setAttribute("ticket", ticket);

        return "order";
    }

    @PostMapping(value = "/order", params = {"hiddenAction=carriage"})
    public String ticketCarriage(@RequestParam(value = "carriageNumber") int carriageNumber,
                                 @SessionAttribute("ticket") TicketDto ticket, Model model) {
        ticket.setCarriageNumber(carriageNumber);

        ticketService.setCarriageComfortLevelForTicket(ticket);

        setModelData(model, ticket);

        return "order";
    }

    @PostMapping(value = "/order", params = {"hiddenAction=seat"})
    public String ticketSeat(@RequestParam(value = "seatNumber") int seatNumber,
                             @SessionAttribute("ticket") TicketDto ticket, Model model) {
        ticket.setSeatNumber(seatNumber);

        ticketService.setTicketFinalPrice(ticket);

        setModelData(model, ticket);

        return "order";
    }

    @PostMapping(value = "/order", params = {"hiddenAction=payment"})
    public String ticketPayment(@SessionAttribute("ticket") TicketDto ticket, HttpSession session,
                                @AuthenticationPrincipal UserDetails userDetails, Model model,
                                @ModelAttribute("card") CreditCard card) {
        UserDto userDto = new UserDto();
        userDto.setUsername(userDetails.getUsername());

        ticket.setUser(userDto);

        try {
            ticketService.payTicket(card, ticket);
        } catch (Exception e) {
            setModelData(model,ticket);

            model.addAttribute("error", true);

            return "order";
        }

        model.addAttribute("ticket", ticket);

        session.removeAttribute("ticket");

        return "successfulOrder";
    }

    private void setModelData(Model model, TicketDto ticket) {
        model.addAttribute("card", new CreditCard());

        if (ticket.getCarriageNumber() != null) {
            List<Seat> seats = departureService.getCarriageSeatsForDeparture(ticket.getDeparture(), ticket.getCarriageNumber());

            model.addAttribute("seats", seats);
        }
    }
}
