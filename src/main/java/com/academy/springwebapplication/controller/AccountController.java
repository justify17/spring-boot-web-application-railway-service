package com.academy.springwebapplication.controller;

import com.academy.springwebapplication.dto.ChangedPasswordDto;
import com.academy.springwebapplication.dto.ChangedUserInformationDto;
import com.academy.springwebapplication.dto.ChangedUsernameDto;
import com.academy.springwebapplication.dto.TicketDto;
import com.academy.springwebapplication.service.AccountService;
import com.academy.springwebapplication.service.TicketService;
import com.academy.springwebapplication.validator.ChangePasswordValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Validator;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class AccountController {
    private final AccountService accountService;
    private final TicketService ticketService;
    private final Validator changePasswordValidator;

    @GetMapping("/account")
    public String account(@AuthenticationPrincipal UserDetails userDetails,
                          Model model) {
        setModelData(model, userDetails.getUsername());

        model.addAttribute("openDefault", true);

        return "account";
    }

    @PostMapping(value = "/account", params = {"hiddenAction=cancelOrder"})
    public String cancelOrder(@RequestParam("ticketId") Integer ticketId,
                              @AuthenticationPrincipal UserDetails userDetails,
                              Model model) {
        ticketService.deleteTicketById(ticketId);

        setModelData(model, userDetails.getUsername());

        model.addAttribute("openDefault", true);

        return "account";
    }

    @PostMapping(value = "/account", params = {"hiddenAction=changeInformation"})
    public String changeUserInformation(@Valid @ModelAttribute("userInformation") ChangedUserInformationDto changedUserInformationDto,
                                        BindingResult bindingResult,
                                        @AuthenticationPrincipal UserDetails userDetails,
                                        Model model) {
        if (bindingResult.hasErrors()) {
            setModelData(model, userDetails.getUsername());

            model.addAttribute("openInformation", true);

            return "account";
        }

        accountService.saveUserInformation(changedUserInformationDto);

        setModelData(model, userDetails.getUsername());

        model.addAttribute("successfulSave", "Data successfully saved!");
        model.addAttribute("openInformation", true);

        return "account";
    }

    @PostMapping(value = "/account", params = {"hiddenAction=changeUsername"})
    public String changeUsername(@Valid @ModelAttribute("changedUsername") ChangedUsernameDto changedUsernameDto,
                                 BindingResult bindingResult,
                                 @AuthenticationPrincipal UserDetails userDetails,
                                 HttpSession session, Model model) {
        if (bindingResult.hasErrors()) {
            setModelData(model, userDetails.getUsername());

            model.addAttribute("openChangeUsername", true);

            return "account";
        }

        accountService.saveNewUsername(changedUsernameDto);

        session.invalidate();

        return "redirect:/account";
    }

    @PostMapping(value = "/account", params = {"hiddenAction=changePassword"})
    public String changePassword(@Valid @ModelAttribute("changedPassword") ChangedPasswordDto changedPasswordDto,
                                 BindingResult bindingResult,
                                 @AuthenticationPrincipal UserDetails userDetails,
                                 HttpSession session, Model model) {
        changePasswordValidator.validate(changedPasswordDto, bindingResult);

        if (bindingResult.hasErrors()) {
            setModelData(model, userDetails.getUsername());

            model.addAttribute("openChangePassword", true);

            return "account";
        }

        accountService.saveNewPassword(changedPasswordDto);

        session.invalidate();

        return "redirect:/account";
    }

    private void setModelData(Model model, String username) {
        List<TicketDto> tickets = ticketService.getUserTickets(username);

        model.addAttribute("tickets", tickets);

        if (model.getAttribute("userInformation") == null) {
            ChangedUserInformationDto changedUserInformationDto = accountService.getUserInformation(username);

            model.addAttribute("userInformation", changedUserInformationDto);
        }

        if (model.getAttribute("changedUsername") == null) {
            ChangedUsernameDto changedUsernameDto = new ChangedUsernameDto();
            changedUsernameDto.setUsername(username);

            model.addAttribute("changedUsername", changedUsernameDto);
        }

        if (model.getAttribute("changedPassword") == null) {
            ChangedPasswordDto changedPasswordDto = new ChangedPasswordDto();
            changedPasswordDto.setUsername(username);

            model.addAttribute("changedPassword", changedPasswordDto);
        }
    }
}
