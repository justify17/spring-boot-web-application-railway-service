package com.academy.springwebapplication.controller;

import com.academy.springwebapplication.dto.UserRegistrationDto;
import com.academy.springwebapplication.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class RegistrationController {
    private final RegistrationService registrationService;

    @GetMapping("/registration")
    public String registration(Model model) {
        model.addAttribute("user", new UserRegistrationDto());

        return "registration";
    }

    @PostMapping("/registration")
    public String userRegistration(@Valid @ModelAttribute("user") UserRegistrationDto user,
                                   BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {

            return "registration";
        }

        registrationService.saveNewUser(user);

        return "redirect:/login";
    }
}
