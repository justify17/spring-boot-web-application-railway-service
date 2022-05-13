package com.academy.springwebapplication.controller;

import com.academy.springwebapplication.model.entity.Departure;
import com.academy.springwebapplication.model.entity.Route;
import com.academy.springwebapplication.model.entity.Station;
import com.academy.springwebapplication.service.DepartureService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class DeparturesController {
    private final DepartureService departureService;

    @GetMapping("/departures")
    public String onlineScoreboard(Model model) {
        model.addAttribute("route",new Route());

        return "departures";
    }

    @PostMapping("/departures")
    public String findingDeparturesForRoute(@ModelAttribute Route route, Model model) {
        List<Departure> departures = departureService.getDeparturesForRoute(route);
        model.addAttribute("departures", departures);

        return "departures";
    }
}
