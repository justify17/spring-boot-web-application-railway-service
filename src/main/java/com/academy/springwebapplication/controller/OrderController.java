package com.academy.springwebapplication.controller;

import com.academy.springwebapplication.model.CreditCard;
import com.academy.springwebapplication.model.entity.Ticket;
import com.academy.springwebapplication.model.entity.User;
import com.academy.springwebapplication.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
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

        if (ticketIndex == null) {
            return "order";
        }

        session.removeAttribute("carriageTickets");

        Ticket ticket = tickets.get(ticketIndex);
        session.setAttribute("ticket", ticket);

        return "order";
    }

    @PostMapping(value = "/order", params = {"hiddenAction=payment"})
    public String ticketPayment(@SessionAttribute("ticket") Ticket ticket, HttpSession session,
                                @AuthenticationPrincipal UserDetails userDetails, Model model,
                                @ModelAttribute("card") CreditCard card) {
        User user = new User();
        user.setUsername(userDetails.getUsername());

        ticket.setUser(user);

        try {
            ticketService.payTicket(card, ticket);
        } catch (Exception e) {
            model.addAttribute("card", new CreditCard());
            model.addAttribute("error", true);
            return "order";
        }

        model.addAttribute("ticket", ticket);

        session.removeAttribute("ticket");
        session.removeAttribute("tickets");

        return "successfulOrder";
    }

    @PostMapping(value = "/order", params = {"hiddenAction=carriage"})
    public String ticketCarriage(@RequestParam(value = "carriageNumber", required = false) int carriageNumber,
                                 @SessionAttribute("ticket") Ticket ticket, Model model, HttpSession session) {
        model.addAttribute("card", new CreditCard());

        List<Ticket> purchasedAndNotPurchasedDepartureTicketsForCarriage =
                ticketService.getPurchasedAndNotPurchasedDepartureTicketsForCarriage(ticket.getDeparture(), carriageNumber);

        ticket.setCarriageNumber(carriageNumber);
        ticketService.setCarriageComfortLevelForTicket(ticket);
        ticketService.setAdditionalTicketPriceForComfortLevelOfCarriage(ticket);

        session.setAttribute("carriageTickets", purchasedAndNotPurchasedDepartureTicketsForCarriage);

        return "order";
    }

    @PostMapping(value = "/order", params = {"hiddenAction=seat"})
    public String ticketSeat(@RequestParam(value = "seatNumber", required = false) int seatNumber,
                             @SessionAttribute("ticket") Ticket ticket, Model model) {
        model.addAttribute("card", new CreditCard());

        ticket.setSeatNumber(seatNumber);

        return "order";
    }
}
