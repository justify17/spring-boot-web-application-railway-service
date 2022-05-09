package com.academy.springwebapplication.controller;

import com.academy.springwebapplication.model.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class LoginController {
    @GetMapping("/login")
    public String registration(){
        return "login";
    }

    @PostMapping("/login")
    public String userRegistration(){
        return "redirect:/registration";
    }
}
