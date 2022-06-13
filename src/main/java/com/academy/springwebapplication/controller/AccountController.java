package com.academy.springwebapplication.controller;


import com.academy.springwebapplication.dto.ChangedAccountDataDto;
import com.academy.springwebapplication.dto.TicketDto;
import com.academy.springwebapplication.dto.ChangedUserInformationDto;
import com.academy.springwebapplication.service.AccountService;
import com.academy.springwebapplication.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;
    private final TicketService ticketService;
    private final Validator changePasswordValidator;
    private final Validator changeUsernameValidator;

    @GetMapping("/account")
    public String account(@AuthenticationPrincipal UserDetails userDetails, Model model) {
        setModelData(model, userDetails.getUsername());

        model.addAttribute("openDefault",true);

        return "account";
    }

    @PostMapping(value = "/account", params = {"hiddenAction=cancelOrder"})
    public String cancelOrder(@RequestParam("ticketId") Integer ticketId, Model model,
                              @AuthenticationPrincipal UserDetails userDetails) {
        ticketService.deleteTicketById(ticketId);

        setModelData(model,userDetails.getUsername());

        model.addAttribute("openDefault",true);

        return "account";
    }

    @PostMapping(value = "/account", params = {"hiddenAction=changeInformation"})
    public String changePersonalInformation(@ModelAttribute("userInformation") ChangedUserInformationDto changedUserInformationDto,
                                            @AuthenticationPrincipal UserDetails userDetails, Model model) {
        accountService.saveUserInformation(changedUserInformationDto);

        setModelData(model, userDetails.getUsername());

        model.addAttribute("successfulSave","Data successfully saved!");
        model.addAttribute("openInformation",true);

        return "account";
    }

    @PostMapping(value = "/account", params = {"hiddenAction=changeUsername"})
    public String changeUsername(@ModelAttribute("changedAccountData") ChangedAccountDataDto changedAccountDataDto,
                                 HttpSession session, BindingResult bindingResult,
                                 @AuthenticationPrincipal UserDetails userDetails, Model model) {
        changeUsernameValidator.validate(changedAccountDataDto,bindingResult);

        if (bindingResult.hasErrors()) {
            setModelData(model, userDetails.getUsername());

            model.addAttribute("openChangeUsername",true);

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

            model.addAttribute("openChangePassword",true);

            return "account";
        }

        accountService.saveNewPassword(changedAccountDataDto);

        session.invalidate();

        return "redirect:/account";
    }

    private void setModelData(Model model, String username) {
        List<TicketDto> tickets = ticketService.getUserTickets(username);

        model.addAttribute("tickets",tickets);

        if (model.getAttribute("userInformation") == null) {
            ChangedUserInformationDto changedUserInformationDto = accountService.getUserInformation(username);

            model.addAttribute("userInformation", changedUserInformationDto);
        }

        if (model.getAttribute("changedAccountData") == null) {
            ChangedAccountDataDto changedAccountDataDto = new ChangedAccountDataDto();
            changedAccountDataDto.setUsername(username);

            model.addAttribute("changedAccountData", changedAccountDataDto);
        }
    }
}
