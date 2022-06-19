package com.academy.springwebapplication.controller;

import com.academy.springwebapplication.dto.DepartureDto;
import com.academy.springwebapplication.dto.TicketDto;
import com.academy.springwebapplication.dto.UserRouteDto;
import com.academy.springwebapplication.mapper.DepartureMapper;
import com.academy.springwebapplication.model.entity.Departure;
import com.academy.springwebapplication.service.DepartureService;
import com.academy.springwebapplication.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.Validator;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class DeparturesController {
    private final DepartureService departureService;
    private final TicketService ticketService;
    private final DepartureMapper departureMapper;

    @GetMapping("/departures")
    public String departures(Model model) {
        model.addAttribute("userRoute", new UserRouteDto());

        return "departures";
    }

    @PostMapping("/departures")
    public String findingDeparturesForRoute(@Valid @ModelAttribute("userRoute") UserRouteDto route,
                                            BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {

            return "departures";
        }

        List<Departure> departures = departureService.getDeparturesForRoute(route);

        List<TicketDto> tickets = ticketService.generateTicketsSuitableForUserRoute(departures, route);

        if (tickets.isEmpty()) {
            bindingResult.reject("error.route", "The entered route for the specified date was not found");

            return "departures";
        } else {

            model.addAttribute("tickets", tickets);
        }

        return "departures";
    }

    @GetMapping("/departures/route")
    public String departureRoute(@RequestParam(name = "departureId") Integer departureId,
                                 Model model) {
        Departure departure = departureService.getDepartureById(departureId);

        DepartureDto departureDto = departureMapper.departureToDepartureDto(departure);

        model.addAttribute("departure", departureDto);

        return "departureRoute";
    }
}
