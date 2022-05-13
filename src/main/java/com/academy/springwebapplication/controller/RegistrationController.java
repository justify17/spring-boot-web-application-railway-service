package com.academy.springwebapplication.controller;

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
        model.addAttribute("user", new User());

        return "registration";
    }

    @PostMapping("/registration")
    public String userRegistration(@ModelAttribute User user, Model model) {
        if (registrationService.isUserExists(user)) {
            model.addAttribute("isUserExists", true);
            return "registration";
        }

        registrationService.saveNewUser(user);
        return "redirect:/login";
    }
}
