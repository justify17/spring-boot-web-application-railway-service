package com.academy.springwebapplication.service.impl;

import com.academy.springwebapplication.dto.*;
import com.academy.springwebapplication.mapper.TicketMapper;
import com.academy.springwebapplication.model.entity.*;
import com.academy.springwebapplication.model.repository.*;
import com.academy.springwebapplication.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {
    private final TicketRepository ticketRepository;
    private final RouteStationRepository routeStationRepository;
    private final UserRepository userRepository;
    private final StationRepository stationRepository;
    private final TicketMapper ticketMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void payTicket(CreditCard card, TicketDto ticketDto) {
        saveTicket(ticketDto);
        moneyTransfer(card);
    }

    private void moneyTransfer(CreditCard card) {
        if (card.getNumber().trim().length() < 16) {
            throw new RuntimeException("Ticket payment error");
        }

        System.out.println("Payment was successful!");
    }

    private void saveTicket(TicketDto ticketDto) {
        Ticket ticket = ticketMapper.ticketDtoToTicket(ticketDto);

        User user = userRepository.findByUsername(ticketDto.getUser().getUsername());
        ticket.setUser(user);

        Station departureStation = stationRepository.findByTitle(ticketDto.getDepartureStation().getTitle());
        ticket.setUserDepartureStation(departureStation);

        Station arrivalStation = stationRepository.findByTitle(ticketDto.getArrivalStation().getTitle());
        ticket.setUserArrivalStation(arrivalStation);

        ticketRepository.save(ticket);

        if (ticket.getId() == null) {
            throw new RuntimeException("Error saving ticket");
        }
    }

    @Override
    public List<TicketDto> getTicketsForDeparturesAlongTheRoute(List<Departure> departures, UserRouteDto userRouteDto) {
        List<TicketDto> tickets = new ArrayList<>();

        for (Departure departure : departures) {
            TicketDto ticketDto = ticketMapper.departureAndUserRouteDtoToTicketDto(departure, userRouteDto);

            setTicketDepartureAndArrivalDates(ticketDto);
            setTicketRoutePricePerDeparture(ticketDto, departure);

            tickets.add(ticketDto);
        }

        return tickets;
    }

    private void setTicketDepartureAndArrivalDates(TicketDto ticketDto) {
        List<StationSchedule> stationSchedules = ticketDto.getDeparture().getStationSchedules();

        for (StationSchedule stationSchedule : stationSchedules) {
            String stationTitle = stationSchedule.getStation().getTitle();

            if (stationTitle.equals(ticketDto.getDepartureStation().getTitle())) {
                ticketDto.setUserDepartureDate(stationSchedule.getDepartureDate());
            } else if (stationTitle.equals(ticketDto.getArrivalStation().getTitle())) {
                ticketDto.setUserArrivalDate(stationSchedule.getArrivalDate());
            }
        }
    }

    private void setTicketRoutePricePerDeparture(TicketDto ticketDto, Departure departure) {
        RouteStation departureRouteStation =
                routeStationRepository.findByRoute_IdAndStation_Title(departure.getRoute().getId(),
                        ticketDto.getDepartureStation().getTitle());
        RouteStation arrivalRouteStation =
                routeStationRepository.findByRoute_IdAndStation_Title(departure.getRoute().getId(),
                        ticketDto.getArrivalStation().getTitle());

        List<RouteStation> routeStations = departure.getRoute().getRouteStations();

        int price = routeStations.stream()
                .filter(routeStation -> routeStation.getRouteStopNumber() >= departureRouteStation.getRouteStopNumber()
                        && routeStation.getRouteStopNumber() < arrivalRouteStation.getRouteStopNumber())
                .mapToInt(RouteStation::getPriceToNextStation)
                .sum();

        ticketDto.setRoutePrice(price);
    }

    @Override
    public boolean isTicketExists(DepartureDto departureDto, Seat seat) {
        Ticket existingTicket = ticketRepository.
                findByDeparture_IdAndCarriageNumberAndSeatNumber
                        (departureDto.getId(), seat.getCarriageNumber(), seat.getNumber());

        if (existingTicket != null) {
            return true;
        }

        return false;
    }

    @Override
    public void setCarriageComfortLevelForTicket(TicketDto ticketDto) {
        CarriageDto carriageDto = ticketDto.getDeparture().getTrain().getCarriages().stream()
                .filter(carriage -> Objects.equals(carriage.getNumber(), ticketDto.getCarriageNumber()))
                .findFirst().get();

        ticketDto.setCarriageComfortLevel(carriageDto.getComfortLevel());
    }

    private void setTicketAdditionalPrice(TicketDto ticketDto) {
        if (ticketDto.getDeparture().getRoute().getType().equals("Региональные линии")) {
            return;
        }

        int additionalPrice = getPricePerCarriageComfortLevel(ticketDto);
        ticketDto.setAdditionalPrice(additionalPrice);
    }

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
}
