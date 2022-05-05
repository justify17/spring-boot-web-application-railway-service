package com.academy.springwebapplication.controller;

import com.academy.springwebapplication.model.entity.Carriage;
import com.academy.springwebapplication.model.entity.Train;
import com.academy.springwebapplication.model.repository.TrainRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MainController {

    @GetMapping(value = "/")
    public String main(){
        return "main";
    }
}
