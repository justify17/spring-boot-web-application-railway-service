package com.academy.springwebapplication.controller;

import com.academy.springwebapplication.model.CreditCard;
import com.academy.springwebapplication.model.entity.Ticket;
import com.academy.springwebapplication.model.entity.User;
import com.academy.springwebapplication.model.repository.TicketRepository;
import com.academy.springwebapplication.service.TicketService;
import com.academy.springwebapplication.service.impl.UserDetailsServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class OrderController {
    private final TicketService ticketService;

    @GetMapping("/order")
    public String order(@SessionAttribute(required = false, name = "tickets") List<Ticket> tickets,
                        @RequestParam(required = false, name = "ticketIndex") Integer ticketIndex,
                        Model model, HttpSession session) {
        model.addAttribute("card", new CreditCard());

        if(ticketIndex == null){
            return "order";
        }

        Ticket ticket = tickets.get(ticketIndex);
        session.setAttribute("ticket", ticket);

        return "order";
    }

    @PostMapping("/order")
    public String ticketPayment(@SessionAttribute("ticket") Ticket ticket, HttpSession session,
                                @AuthenticationPrincipal UserDetails userDetails, Model model) {
        User user = new User();
        user.setUsername(userDetails.getUsername());

        ticket.setUser(user);

        ticketService.saveTicket(ticket);

        model.addAttribute("ticket",ticket);
        session.removeAttribute("ticket");
        session.removeAttribute("tickets");

        return "successfulOrder";
    }
}
