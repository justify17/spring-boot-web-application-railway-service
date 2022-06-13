package com.academy.springwebapplication.controller;

import com.academy.springwebapplication.dto.*;
import com.academy.springwebapplication.service.AdminService;
import com.academy.springwebapplication.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;
    private final TicketService ticketService;

    @GetMapping("/admin")
    public String admin(Model model) {
        setModelData(model);

        model.addAttribute("openUserDetails", true);

        return "admin";
    }

    @PostMapping(value = "/admin", params = {"hiddenAction=changeAccountStatus"})
    public String changeAccountStatus(@RequestParam("username") String username, Model model) {
        adminService.setNewAccountStatus(username);

        setModelData(model);

        model.addAttribute("openUserDetails", true);

        return "admin";
    }

    @PostMapping(value = "/admin", params = {"hiddenAction=changeUserRole"})
    public String changeUserRole(@RequestParam("username") String username,
                                 @RequestParam("newRole") Integer newRoleId, Model model) {
        adminService.setNewUserRole(username,newRoleId);

        setModelData(model);

        model.addAttribute("openUserDetails", true);

        return "admin";
    }

    @GetMapping("/admin/userOrders")
    public String userOrders(@RequestParam("username") String username, Model model) {
        List<TicketDto> userTickets = ticketService.getUserTickets(username);

        model.addAttribute("tickets",userTickets);

        return "userOrders";
    }

    @PostMapping(value = "/admin/userOrders")
    public String deleteUserOrder(@RequestParam("ticketId") Integer ticketId,
                                  @RequestParam("username") String username, Model model) {
        ticketService.deleteTicketById(ticketId);

        List<TicketDto> userTickets = ticketService.getUserTickets(username);

        model.addAttribute("tickets",userTickets);

        return "userOrders";
    }

    private void setModelData(Model model) {
        List<UserDto> users = adminService.getAllUsers();

        model.addAttribute("users", users);

        List<RoleDto> roles = adminService.getAllRoles();

        model.addAttribute("roles", roles);
    }
}
