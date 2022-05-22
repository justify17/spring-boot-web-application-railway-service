package com.academy.springwebapplication.service.impl;

import com.academy.springwebapplication.model.StationSchedule;
import com.academy.springwebapplication.model.entity.*;
import com.academy.springwebapplication.model.repository.*;
import com.academy.springwebapplication.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {
    private final TicketRepository ticketRepository;
    private final RouteStationRepository routeStationRepository;
    private final UserRepository userRepository;
    private final StationRepository stationRepository;
    private final TrainCarriageRepository trainCarriageRepository;

    @Override
    public void saveTicket(Ticket ticket) {
        User user = userRepository.findByUsername(ticket.getUser().getUsername());
        ticket.setUser(user);

        Station departureStation = stationRepository.findByTitle(ticket.getUserDepartureStation().getTitle());
        ticket.setUserDepartureStation(departureStation);

        Station arrivalStation = stationRepository.findByTitle(ticket.getUserArrivalStation().getTitle());
        ticket.setUserArrivalStation(arrivalStation);

        ticketRepository.save(ticket);
    }

    @Override
    public List<Ticket> getTicketsForDeparturesAlongTheRoute(List<Departure> departures, Route route) {
        List<Ticket> tickets = new ArrayList<>();

        for (Departure departure : departures) {
            Ticket ticket = new Ticket();

            ticket.setUserDepartureStation(route.getDepartureStation());
            ticket.setUserArrivalStation(route.getArrivalStation());
            ticket.setDeparture(departure);

            setTicketPriceForDeparture(ticket, departure);
            setTicketUserDepartureAndArrivalDateForDeparture(ticket, departure);

            tickets.add(ticket);
        }

        return tickets;
    }

    private void setTicketPriceForDeparture(Ticket ticket, Departure departure) {
        RouteStation departureRouteStation =
                routeStationRepository.findByRoute_IdAndStation_Title(departure.getRoute().getId(),
                        ticket.getUserDepartureStation().getTitle());
        RouteStation arrivalRouteStation =
                routeStationRepository.findByRoute_IdAndStation_Title(departure.getRoute().getId(),
                        ticket.getUserArrivalStation().getTitle());

        List<RouteStation> routeStations = departure.getRoute().getRouteStations();

        int price = routeStations.stream()
                .filter(routeStation -> routeStation.getRouteStopNumber() >= departureRouteStation.getRouteStopNumber()
                        && routeStation.getRouteStopNumber() < arrivalRouteStation.getRouteStopNumber())
                .mapToInt(RouteStation::getPriceToNextStation)
                .sum();

        ticket.setPrice(price);
    }

    private void setTicketUserDepartureAndArrivalDateForDeparture(Ticket ticket, Departure departure) {
        List<StationSchedule> stationSchedules = departure.getStationSchedules();

        for (StationSchedule stationSchedule : stationSchedules) {
            String stationTitle = stationSchedule.getStation().getTitle();

            if (stationTitle.equals(ticket.getUserDepartureStation().getTitle())) {
                ticket.setUserDepartureDate(stationSchedule.getDepartureDate());
            } else if (stationTitle.equals(ticket.getUserArrivalStation().getTitle())) {
                ticket.setUserArrivalDate(stationSchedule.getArrivalDate());
            }
        }
    }

    public List<Ticket> getPurchasedAndNotPurchasedDepartureTicketsForCarriage(Departure departure, int carriageNumber){
        List<Ticket> allPossibleDepartureTicketsForCarriage =
                getAllPossibleDepartureTicketsForCarriage(departure,carriageNumber);

        return allPossibleDepartureTicketsForCarriage.stream()
                .peek(this::setTicketIdIfItPurchased)
                .collect(Collectors.toList());
    }

    private List<Ticket> getAllPossibleDepartureTicketsForCarriage(Departure departure, int carriageNumber){
        List<Ticket> tickets = new ArrayList<>();

        TrainCarriage trainCarriage = trainCarriageRepository.
                findByTrain_IdAndCarriageNumber(departure.getTrain().getId(),carriageNumber);

        int numberOfSeats = trainCarriage.getCarriage().getSeats();

        for (int i = 1; i <= numberOfSeats; i++) {
            Ticket ticket = new Ticket();

            ticket.setDeparture(departure);
            ticket.setCarriageNumber(carriageNumber);
            ticket.setSeatNumber(i);

            tickets.add(ticket);
        }

        return tickets;
    }

    private void setTicketIdIfItPurchased(Ticket ticket){
        Ticket existingTicket = ticketRepository.
                findByDeparture_IdAndCarriageNumberAndSeatNumber
                        (ticket.getDeparture().getId(),ticket.getCarriageNumber(),ticket.getSeatNumber());

        if(existingTicket != null){
            ticket.setId(existingTicket.getId());
        }
    }
}
