package com.academy.springwebapplication.controller;

import com.academy.springwebapplication.model.entity.Departure;
import com.academy.springwebapplication.model.entity.Route;
import com.academy.springwebapplication.service.DepartureService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class DeparturesController {
    private final DepartureService departureService;

    @GetMapping("/departures")
    public String departures(Model model) {
        model.addAttribute("userRoute",new Route());

        return "departures";
    }

    @PostMapping("/departures")
    public String findingDeparturesForRoute(@ModelAttribute("userRoute") Route route, Model model) {
        List<Departure> departures = departureService.getDeparturesWithScheduleForRoute(route);
        model.addAttribute("departures", departures);

        Map<Departure, Integer> prices = departureService.getPricesForDeparturesAlongTheRoute(departures,route);
        model.addAttribute("prices", prices);

        return "departures";
    }

    @GetMapping("/departures/route")
    public String departureRoute(@ModelAttribute("departure") Departure departure) {
        departureService.setStationSchedulesForDeparture(departure);

        return "departureRoute";
    }
}
