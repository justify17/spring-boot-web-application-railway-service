package com.academy.springwebapplication.controller;

import com.academy.springwebapplication.model.entity.Departure;
import com.academy.springwebapplication.model.entity.User;
import com.academy.springwebapplication.service.DepartureServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class DepartureController {
    private final DepartureServiceImpl departureService;

    @GetMapping("/departures")
    public String registration(){
        List<Departure> departures = departureService.getDepartures();
        return "departures";
    }
}
