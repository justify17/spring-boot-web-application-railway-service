package com.academy.springwebapplication.controller;


import com.academy.springwebapplication.dto.UserInformationDto;
import com.academy.springwebapplication.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;

    @GetMapping("/account")
    public String account(@AuthenticationPrincipal UserDetails userDetails, Model model){
        String username = userDetails.getUsername();

        UserInformationDto userInformationDto = accountService.getUserInformation(username);

        model.addAttribute("userInformation",userInformationDto);

        return "account";
    }

    @PostMapping("/account")
    public String personalInformationChanges(@ModelAttribute("userInformation") UserInformationDto userInformationDto,
                                             @AuthenticationPrincipal UserDetails userDetails){
        String username = userDetails.getUsername();

        accountService.saveUserInformation(userInformationDto,username);

        return "account";
    }
}
