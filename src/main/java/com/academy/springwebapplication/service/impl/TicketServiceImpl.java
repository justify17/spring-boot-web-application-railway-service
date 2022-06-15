package com.academy.springwebapplication.service.impl;

import com.academy.springwebapplication.dto.*;
import com.academy.springwebapplication.mapper.DepartureMapper;
import com.academy.springwebapplication.mapper.TicketMapper;
import com.academy.springwebapplication.model.entity.*;
import com.academy.springwebapplication.model.repository.RouteStationRepository;
import com.academy.springwebapplication.model.repository.StationRepository;
import com.academy.springwebapplication.model.repository.TicketRepository;
import com.academy.springwebapplication.model.repository.UserRepository;
import com.academy.springwebapplication.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TicketServiceImpl implements TicketService {
    private final TicketRepository ticketRepository;
    private final RouteStationRepository routeStationRepository;
    private final UserRepository userRepository;
    private final StationRepository stationRepository;
    private final TicketMapper ticketMapper;
    private final DepartureMapper departureMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void payTicket(CreditCard card, TicketDto ticketDto) {
        saveTicket(ticketDto);
        moneyTransfer(card);
    }

    private void moneyTransfer(CreditCard card) {

        System.out.printf("Payment by card: %s was successful!",card.getNumber());
    }

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
            throw new RuntimeException("Error saving ticket");
        }
    }

    @Override
    public List<TicketDto> generateTicketsSuitableForUserRoute(List<Departure> departures, UserRouteDto userRouteDto) {

        return departures.stream()
                .map(departure -> createTicketForDepartureAlongRoute(departure, userRouteDto))
                .filter(ticketDto -> isTicketSuitableForUserRoute(ticketDto, userRouteDto))
                .sorted(Comparator.comparing(TicketDto::getDepartureDate))
                .collect(Collectors.toList());
    }

    @Override
    public TicketDto createTicketForDepartureAlongRoute(Departure departure, UserRouteDto userRouteDto) {
        TicketDto ticketDto = new TicketDto();

        ticketDto.setDeparture(departureMapper.departureToDepartureDto(departure));
        ticketDto.setDepartureStation(userRouteDto.getDepartureStation());
        ticketDto.setArrivalStation(userRouteDto.getArrivalStation());

        setTicketDepartureAndArrivalDates(ticketDto);
        setTicketRoutePricePerDeparture(ticketDto, departure);

        return ticketDto;
    }

    private boolean isTicketSuitableForUserRoute(TicketDto ticketDto, UserRouteDto userRouteDto) {
        if (userRouteDto.getDepartureDate() == null && userRouteDto.getDepartureTime() == null) {
            LocalDateTime userDepartureDateTime = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);

            return ticketDto.getDepartureDate().isAfter(userDepartureDateTime);
        } else {

            return isTicketDepartureDateEqualUserDepartureDate(ticketDto, userRouteDto);
        }
    }

    private boolean isTicketDepartureDateEqualUserDepartureDate(TicketDto ticketDto, UserRouteDto userRouteDto) {
        LocalDateTime userDepartureDateTime;
        LocalDateTime endOfUserDepartureDay;

        if (userRouteDto.getDepartureDate() != null && userRouteDto.getDepartureTime() != null) {
            userDepartureDateTime = LocalDateTime.of(userRouteDto.getDepartureDate(), userRouteDto.getDepartureTime());

            endOfUserDepartureDay = LocalDateTime.of(userRouteDto.getDepartureDate(), LocalTime.MAX);
        } else if (userRouteDto.getDepartureTime() == null) {
            userDepartureDateTime = LocalDateTime.of(userRouteDto.getDepartureDate(), LocalTime.MIN);

            endOfUserDepartureDay = LocalDateTime.of(userRouteDto.getDepartureDate(), LocalTime.MAX);
        } else {
            userDepartureDateTime = LocalDateTime.of(LocalDate.now(), userRouteDto.getDepartureTime());

            endOfUserDepartureDay = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
        }

        return ticketDto.getDepartureDate().isAfter(userDepartureDateTime) &&
                ticketDto.getDepartureDate().isBefore(endOfUserDepartureDay);
    }

    private void setTicketDepartureAndArrivalDates(TicketDto ticketDto) {
        List<RouteStationDto> routeStations = ticketDto.getDeparture().getRoute().getRouteStations();

        for (RouteStationDto routeStationDto : routeStations) {
            String stationTitle = routeStationDto.getStation().getTitle();

            if (stationTitle.equals(ticketDto.getDepartureStation().getTitle())) {
                ticketDto.setDepartureDate(routeStationDto.getDepartureDate());
            } else if (stationTitle.equals(ticketDto.getArrivalStation().getTitle())) {
                ticketDto.setArrivalDate(routeStationDto.getArrivalDate());
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
        if (ticketDto.getCarriageNumber() != null) {
            CarriageDto carriageDto = ticketDto.getDeparture().getTrain().getCarriages().stream()
                    .filter(carriage -> Objects.equals(carriage.getNumber(), ticketDto.getCarriageNumber()))
                    .findFirst().get();

            ticketDto.setCarriageComfortLevel(carriageDto.getComfortLevel());
        }
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

    @Override
    public List<TicketDto> getUserTickets(String username) {
        List<Ticket> tickets = ticketRepository.findByUser_Username(username);

        return tickets.stream()
                .map(ticketMapper::ticketToTicketDto)
                .peek(this::setCarriageComfortLevelForTicket)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteTicketById(Integer ticketId) {
        ticketRepository.deleteById(ticketId);
    }

    @Override
    public int getNumberOfPurchasedTicketsForDeparture(Integer departureId) {

        return ticketRepository.countAllByDeparture_Id(departureId);
    }

    @Override
    public List<TicketDto> getAllTicketsByDeparture(Integer departureId) {
        List<Ticket> tickets = ticketRepository.findByDeparture_Id(departureId);

        return tickets.stream()
                .map(ticketMapper::ticketToTicketDto)
                .peek(this::setCarriageComfortLevelForTicket)
                .collect(Collectors.toList());
    }
}
