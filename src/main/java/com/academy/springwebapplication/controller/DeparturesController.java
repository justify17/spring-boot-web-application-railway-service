package com.academy.springwebapplication.controller;

import com.academy.springwebapplication.model.entity.Departure;
import com.academy.springwebapplication.model.entity.Route;
import com.academy.springwebapplication.model.entity.Ticket;
import com.academy.springwebapplication.service.DepartureService;
import com.academy.springwebapplication.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class DeparturesController {
    private final DepartureService departureService;
    private final TicketService ticketService;

    @GetMapping("/departures")
    public String departures(Model model) {
        model.addAttribute("userRoute",new Route());

        return "departures";
    }

    @PostMapping("/departures")
    public String findingDeparturesForRoute(@ModelAttribute("userRoute") Route route, HttpSession session) {
        List<Departure> departures = departureService.getDeparturesWithScheduleForRoute(route);

        List<Ticket> tickets = ticketService.getTicketsForDeparturesAlongTheRoute(departures,route);
        session.setAttribute("tickets", tickets);

        return "departures";
    }

    @GetMapping("/departures/route")
    public String departureRoute(@ModelAttribute("departure") Departure departure) {
        departureService.setStationSchedulesForDeparture(departure);

        return "departureRoute";
    }
}
