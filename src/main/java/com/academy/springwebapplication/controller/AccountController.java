package com.academy.springwebapplication.controller;


import com.academy.springwebapplication.dto.ChangedAccountDataDto;
import com.academy.springwebapplication.dto.UserInformationDto;
import com.academy.springwebapplication.service.AccountService;
import com.academy.springwebapplication.validator.ChangePasswordValidator;
import com.academy.springwebapplication.validator.ChangeUsernameValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;
    private final ChangePasswordValidator changePasswordValidator;
    private final ChangeUsernameValidator changeUsernameValidator;

    @GetMapping("/account")
    public String account(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        setModelData(model, userDetails.getUsername());

        return "account";
    }

    @PostMapping(value = "/account", params = {"hiddenAction=changeInformation"})
    public String changePersonalInformation(@ModelAttribute("userInformation") UserInformationDto userInformationDto,
                                            @AuthenticationPrincipal UserDetails userDetails, Model model) {
        accountService.saveUserInformation(userInformationDto);

        setModelData(model, userDetails.getUsername());

        model.addAttribute("successfulSave","Data successfully saved!");

        return "account";
    }

    @PostMapping(value = "/account", params = {"hiddenAction=changeUsername"})
    public String changeUsername(@ModelAttribute("changedAccountData") ChangedAccountDataDto changedAccountDataDto,
                                 HttpSession session, BindingResult bindingResult,
                                 @AuthenticationPrincipal UserDetails userDetails, Model model) {
        changeUsernameValidator.validate(changedAccountDataDto,bindingResult);

        if (bindingResult.hasErrors()) {
            setModelData(model, userDetails.getUsername());

            return "account";
        }

        accountService.saveNewUsername(changedAccountDataDto);

        session.invalidate();

        return "redirect:/account";
    }

    @PostMapping(value = "/account", params = {"hiddenAction=changePassword"})
    public String changePassword(@ModelAttribute("changedAccountData") ChangedAccountDataDto changedAccountDataDto,
                                 HttpSession session, BindingResult bindingResult,
                                 @AuthenticationPrincipal UserDetails userDetails, Model model) {
        changePasswordValidator.validate(changedAccountDataDto, bindingResult);

        if (bindingResult.hasErrors()) {
            setModelData(model, userDetails.getUsername());

            return "account";
        }

        accountService.saveNewPassword(changedAccountDataDto);

        session.invalidate();

        return "redirect:/account";
    }

    private void setModelData(Model model, String username) {
        if (model.getAttribute("userInformation") == null) {
            UserInformationDto userInformationDto = accountService.getUserInformation(username);

            model.addAttribute("userInformation", userInformationDto);
        }

        if (model.getAttribute("changedAccountData") == null) {
            ChangedAccountDataDto changedAccountDataDto = new ChangedAccountDataDto();
            changedAccountDataDto.setUsername(username);

            model.addAttribute("changedAccountData", changedAccountDataDto);
        }
    }
}
