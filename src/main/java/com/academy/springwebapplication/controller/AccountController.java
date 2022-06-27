package com.academy.springwebapplication.controller;

import com.academy.springwebapplication.dto.ChangedPasswordDto;
import com.academy.springwebapplication.dto.ChangedUserInformationDto;
import com.academy.springwebapplication.dto.ChangedUsernameDto;
import com.academy.springwebapplication.dto.TicketDto;
import com.academy.springwebapplication.exception.EntityByIdNotFoundException;
import com.academy.springwebapplication.service.AccountService;
import com.academy.springwebapplication.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
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

    @GetMapping("/account")
    public String account(@AuthenticationPrincipal UserDetails userDetails,
                          Model model) {
        setModelData(model, userDetails.getUsername());

        model.addAttribute("openDefault", true);

        return "account";
    }

    @PostMapping(value = "/account", params = {"hiddenAction=cancelOrder"})
    public String cancelOrder(@RequestParam("ticketId") int ticketId,
                              @AuthenticationPrincipal UserDetails userDetails,
                              Model model) {
        ticketService.checkIfTicketIdIsValid(ticketId);

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
        setModelData(model, userDetails.getUsername());

        model.addAttribute("openInformation", true);

        if (bindingResult.hasErrors()) {

            return "account";
        }

        accountService.saveNewUserInformation(changedUserInformationDto);

        model.addAttribute("successfulSave", "Data successfully saved!");

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

        return "redirect:/login";
    }

    @PostMapping(value = "/account", params = {"hiddenAction=changePassword"})
    public String changePassword(@Valid @ModelAttribute("changedPassword") ChangedPasswordDto changedPasswordDto,
                                 BindingResult bindingResult,
                                 @AuthenticationPrincipal UserDetails userDetails,
                                 HttpSession session, Model model) {
        if (bindingResult.hasErrors()) {
            setModelData(model, userDetails.getUsername());

            model.addAttribute("openChangePassword", true);

            return "account";
        }

        accountService.saveNewPassword(changedPasswordDto);


        session.invalidate();

        return "redirect:/login";
    }

    private void setModelData(Model model, String username) {
        List<TicketDto> tickets = ticketService.getUserTickets(username);

        model.addAttribute("tickets", tickets);

        if (!model.containsAttribute("userInformation")) {
            ChangedUserInformationDto changedUserInformationDto = accountService.getUserInformation(username);

            model.addAttribute("userInformation", changedUserInformationDto);
        }

        if (!model.containsAttribute("changedUsername")) {
            ChangedUsernameDto changedUsernameDto = new ChangedUsernameDto();
            changedUsernameDto.setUsername(username);

            model.addAttribute("changedUsername", changedUsernameDto);
        }

        if (!model.containsAttribute("changedPassword")) {
            ChangedPasswordDto changedPasswordDto = new ChangedPasswordDto();
            changedPasswordDto.setUsername(username);

            model.addAttribute("changedPassword", changedPasswordDto);
        }
    }
}
