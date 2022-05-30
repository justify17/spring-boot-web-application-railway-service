package com.academy.springwebapplication.controller;

import com.academy.springwebapplication.dto.DepartureDto;
import com.academy.springwebapplication.dto.TicketDto;
import com.academy.springwebapplication.dto.UserRouteDto;
import com.academy.springwebapplication.model.entity.Departure;
import com.academy.springwebapplication.service.DepartureService;
import com.academy.springwebapplication.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class DeparturesController {
    private final DepartureService departureService;
    private final TicketService ticketService;

    @GetMapping("/departures")
    public String departures(Model model, HttpSession session) {
        model.addAttribute("userRoute", new UserRouteDto());

        session.removeAttribute("tickets");

        return "departures";
    }

    @PostMapping("/departures")
    public String findingDeparturesForRoute(@ModelAttribute("userRoute") UserRouteDto route, HttpSession session) {
        List<Departure> departures = departureService.getDeparturesForRoute(route);

        List<TicketDto> tickets = ticketService.getTicketsForDeparturesAlongTheRoute(departures, route);
        session.setAttribute("tickets", tickets);

        return "departures";
    }

    @GetMapping("/departures/route")
    public String departureRoute(@SessionAttribute(name = "tickets") List<TicketDto> tickets,
                                 @RequestParam(name = "ticketIndex") Integer ticketIndex, Model model) {
        DepartureDto departure = tickets.get(ticketIndex).getDeparture();

        model.addAttribute("departure",departure);

        return "departureRoute";
    }
}
