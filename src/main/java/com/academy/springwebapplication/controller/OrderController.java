package com.academy.springwebapplication.controller;

import com.academy.springwebapplication.model.entity.Departure;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

@Controller
@RequiredArgsConstructor
public class OrderController {

    @GetMapping("/departures/order")
    public String main(@ModelAttribute Departure departure){
        return "order";
    }
}
