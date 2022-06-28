package com.academy.springwebapplication.service.impl;

import com.academy.springwebapplication.dto.CarriageDto;
import com.academy.springwebapplication.dto.CreditCardDto;
import com.academy.springwebapplication.dto.SeatDto;
import com.academy.springwebapplication.dto.TicketDto;
import com.academy.springwebapplication.exception.FailedPaymentException;
import com.academy.springwebapplication.exception.FailedSavingTicketException;
import com.academy.springwebapplication.mapper.TicketMapper;
import com.academy.springwebapplication.model.entity.Station;
import com.academy.springwebapplication.model.entity.Ticket;
import com.academy.springwebapplication.model.entity.User;
import com.academy.springwebapplication.model.repository.StationRepository;
import com.academy.springwebapplication.model.repository.TicketRepository;
import com.academy.springwebapplication.model.repository.UserRepository;
import com.academy.springwebapplication.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * This class is responsible for ordering user tickets
 */
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final StationRepository stationRepository;
    private final TicketMapper ticketMapper;

    /**
     * Returns information about seats in the user's ticket carriage
     *
     * @param ticketDto the user ticket
     * @return Seats in the user's ticket carriage
     */
    @Override
    public List<SeatDto> getSeatsByTicketData(TicketDto ticketDto) {
        List<SeatDto> seats = new ArrayList<>();

        Integer ticketSeatNumber = ticketDto.getSeatNumber() != null ? ticketDto.getSeatNumber() : null;

        int numberOfSeats = getNumberOfSeatsInCarriage(ticketDto);

        for (int i = 1; i <= numberOfSeats; i++) {
            SeatDto seat = new SeatDto();
            seat.setNumber(i);
            seat.setCarriageNumber(ticketDto.getCarriageNumber());

            ticketDto.setSeatNumber(i);

            boolean seatFree = !isTicketExisting(ticketDto);
            seat.setFree(seatFree);

            seats.add(seat);
        }

        ticketDto.setSeatNumber(ticketSeatNumber);

        return seats;
    }

    /**
     * Returns the number of seats in the user's ticket carriage
     *
     * @param ticketDto the user ticket
     * @return number of seats in the carriage
     */
    private int getNumberOfSeatsInCarriage(TicketDto ticketDto) {
        CarriageDto ticketCarriage = ticketDto.getDeparture().getTrain().getCarriages().stream()
                .filter(carriage -> carriage.getNumber() == ticketDto.getCarriageNumber())
                .findFirst().get();

        return ticketCarriage.getNumberOfSeats();
    }

    /**
     * Checks if there is the ticket for the given departure, time, carriage and seat.
     * This method is needed in order to find out if the seat is free for the departure, specific time and carriage
     *
     * @param ticketDto the user ticket
     * @return true - if ticket is existing, false - if ticket is not existing
     */
    private boolean isTicketExisting(TicketDto ticketDto) {
        List<Ticket> existingTickets = ticketRepository.findByTicketData(ticketDto.getDeparture().getId(),
                ticketDto.getCarriageNumber(), ticketDto.getSeatNumber(),
                ticketDto.getDepartureDate(), ticketDto.getArrivalDate());

        return !existingTickets.isEmpty();
    }

    /**
     * Sets the carriage comfort level for the user ticket
     *
     * @param ticketDto the user ticket
     */
    @Override
    public void setCarriageComfortLevelForTicket(TicketDto ticketDto) {
        if (ticketDto.getCarriageNumber() != null) {
            CarriageDto carriageDto = ticketDto.getDeparture().getTrain().getCarriages().stream()
                    .filter(carriage -> Objects.equals(carriage.getNumber(), ticketDto.getCarriageNumber()))
                    .findFirst().get();

            ticketDto.setCarriageComfortLevel(carriageDto.getComfortLevel());
        }
    }

    /**
     * Sets the final price of the user ticket
     *
     * @param ticketDto the user ticket
     */
    @Override
    public void setTicketFinalPrice(TicketDto ticketDto) {
        setTicketAdditionalPrice(ticketDto);

        double finalPrice;

        if (ticketDto.getAdditionalPrice() != null) {
            finalPrice = (ticketDto.getRoutePrice() + ticketDto.getAdditionalPrice()) / 100.0;
        } else {
            finalPrice = ticketDto.getRoutePrice() / 100.0;
        }

        ticketDto.setFinalPrice(finalPrice);
    }

    /**
     * Sets the additional price of the user ticket.
     * In this case, the additional price depends on the comfort level of the carriage
     *
     * @param ticketDto the user ticket
     */
    private void setTicketAdditionalPrice(TicketDto ticketDto) {
        if (ticketDto.getDeparture().getRoute().getType().equals("Региональные линии")) {
            return;
        }

        int additionalPrice = getPricePerCarriageComfortLevel(ticketDto);

        ticketDto.setAdditionalPrice(additionalPrice);
    }

    /**
     * Returns price for the level of carriage comfort
     *
     * @param ticketDto the user ticket
     * @return additional price for the level of carriage comfort
     */
    private int getPricePerCarriageComfortLevel(TicketDto ticketDto) {
        String carriageComfortLevel = ticketDto.getCarriageComfortLevel();

        switch (carriageComfortLevel) {
            case "LUX":
                return ticketDto.getRoutePrice() / 100 * 50;
            case "COUPE":
                return ticketDto.getRoutePrice() / 100 * 30;
            case "ECONOMY":
                return ticketDto.getRoutePrice() / 100 * 5;
        }

        return 0;
    }

    /**
     * User ticket payment operation
     *
     * @param card      the credit card to pay for the ticket
     * @param ticketDto the user ticket
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void payTicket(CreditCardDto card, TicketDto ticketDto) {
        saveTicket(ticketDto);
        moneyWriteOff(card, ticketDto.getFinalPrice());
    }

    /**
     * Saves the user ticket in the database
     *
     * @param ticketDto the user ticket to save
     */
    private void saveTicket(TicketDto ticketDto) {
        Ticket ticket = ticketMapper.ticketDtoToTicket(ticketDto);

        User user = userRepository.findByUsername(ticketDto.getUsername());
        ticket.setUser(user);

        Station departureStation = stationRepository.findByTitle(ticketDto.getDepartureStation().getTitle());
        ticket.setUserDepartureStation(departureStation);


        Station arrivalStation = stationRepository.findByTitle(ticketDto.getArrivalStation().getTitle());
        ticket.setUserArrivalStation(arrivalStation);

        ticketRepository.save(ticket);

        if (ticket.getId() == null) {
            throw new FailedSavingTicketException(ticketDto.getUsername());
        }
    }

    /**
     * Simulates withdrawing money from the credit card
     *
     * @param card  The credit card to withdraw money
     * @param price Price for the ticket
     */
    private void moneyWriteOff(CreditCardDto card, double price) {
        if (card.getNumber().isEmpty()) {
            throw new FailedPaymentException(card.getNumber());
        }

        System.out.printf("Payment by card: %s was successful! Written off %.2f BYN.", card.getNumber(), price);
    }
}
