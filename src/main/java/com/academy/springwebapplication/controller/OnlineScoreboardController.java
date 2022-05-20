package com.academy.springwebapplication.controller;

import com.academy.springwebapplication.model.entity.Departure;
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
public class OnlineScoreboardController {
    private final DepartureService departureService;

    @GetMapping("/onlineScoreboard")
    public String onlineScoreboard(Model model) {
        model.addAttribute("userStation",new Station());

        return "onlineScoreboard";
    }

    @PostMapping("/onlineScoreboard")
    public String findingDeparturesForStation(@ModelAttribute("userStation") Station station, Model model) {
        List<Departure> departures = departureService.getDeparturesByStation(station);
        model.addAttribute("departures", departures);

        return "onlineScoreboard";
    }
}
