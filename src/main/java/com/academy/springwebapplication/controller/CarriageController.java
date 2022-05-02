package com.academy.springwebapplication.controller;

import com.academy.springwebapplication.model.entity.Carriage;
import com.academy.springwebapplication.model.repository.CarriageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CarriageController {
    private final CarriageRepository carriageRepository;

    @GetMapping(value = "/carriages")
    public String carriages(){
        List<Carriage> carriages = carriageRepository.findAll();

        return "carriages";
    }
}
