package com.academy.springwebapplication.controller;

import com.academy.springwebapplication.dto.DepartureDto;
import com.academy.springwebapplication.dto.StationDto;
import com.academy.springwebapplication.service.DepartureService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class OnlineScoreboardController {
    private final DepartureService departureService;

    @GetMapping("/onlineScoreboard")
    public String onlineScoreboard(Model model) {
        model.addAttribute("station",new StationDto());

        return "onlineScoreboard";
    }

    @PostMapping("/onlineScoreboard")
    public String findingDeparturesByStation(@Valid @ModelAttribute("station") StationDto station,
                                             BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {

            return "onlineScoreboard";
        }

        List<DepartureDto> departures = departureService.getDeparturesByStation(station);

        model.addAttribute("departures", departures);

        return "onlineScoreboard";
    }
}
