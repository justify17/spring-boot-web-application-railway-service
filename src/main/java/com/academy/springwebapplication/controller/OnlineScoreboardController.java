package com.academy.springwebapplication.controller;

import com.academy.springwebapplication.model.entity.Departure;
import com.academy.springwebapplication.model.entity.Station;
import com.academy.springwebapplication.service.DepartureServiceImpl;
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
    private final DepartureServiceImpl departureService;

    @GetMapping("/onlineScoreboard")
    public String onlineScoreboard(Model model) {
        model.addAttribute("station", new Station());

        List<Departure> departures = departureService.getAllDepartures();
        model.addAttribute("departures", departures);
        return "onlineScoreboard";
    }

    @PostMapping("/onlineScoreboard")
    public String findingDeparturesForStation(@ModelAttribute Station station, Model model) {
        String stationTitle = station.getTitle();
        List<Departure> departures = departureService.getDeparturesFromAndToStation(stationTitle);
        model.addAttribute("departures", departures);
        return "onlineScoreboard";
    }
}
