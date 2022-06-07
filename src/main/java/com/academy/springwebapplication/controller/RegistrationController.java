package com.academy.springwebapplication.controller;

import com.academy.springwebapplication.dto.UserDto;
import com.academy.springwebapplication.model.entity.User;
import com.academy.springwebapplication.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class RegistrationController {
    private final RegistrationService registrationService;

    @GetMapping("/registration")
    public String registration(Model model) {
        model.addAttribute("user", new UserDto());

        return "registration";
    }

    @PostMapping("/registration")
    public String userRegistration(@ModelAttribute("user") UserDto user, Model model) {
        if (registrationService.isUserExists(user.getUsername())) {
            model.addAttribute("isUserExists", true);

            return "registration";
        }

        registrationService.saveNewUser(user);

        return "redirect:/login";
    }
}
