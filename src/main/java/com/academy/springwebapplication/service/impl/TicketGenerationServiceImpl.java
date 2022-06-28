package com.academy.springwebapplication.service.impl;

import com.academy.springwebapplication.dto.RouteStationDto;
import com.academy.springwebapplication.dto.TicketDto;
import com.academy.springwebapplication.dto.UserRouteDto;
import com.academy.springwebapplication.mapper.DepartureMapper;
import com.academy.springwebapplication.model.entity.Departure;
import com.academy.springwebapplication.model.entity.RouteStation;
import com.academy.springwebapplication.model.repository.RouteStationRepository;
import com.academy.springwebapplication.service.OrderService;
import com.academy.springwebapplication.service.TicketGenerationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class is responsible for generating potential tickets for the user
 */
@Service
@RequiredArgsConstructor
public class TicketGenerationServiceImpl implements TicketGenerationService {
    private final RouteStationRepository routeStationRepository;
    private final DepartureMapper departureMapper;
    private final OrderService orderService;

    /**
     * Generates tickets suitable for the given route based on departures
     *
     * @param departures   departures for which tickets are generated
     * @param userRouteDto the user route
     * @return tickets
     */
    @Override
    public List<TicketDto> generateTicketsSuitableForUserRoute(List<Departure> departures, UserRouteDto userRouteDto) {

        return departures.stream()
                .map(departure -> createTicketForDepartureAlongRoute(departure, userRouteDto))
                .filter(ticketDto -> isTicketSuitableForUserRoute(ticketDto, userRouteDto))
                .sorted(Comparator.comparing(TicketDto::getDepartureDate))
                .collect(Collectors.toList());
    }

    /**
     * Creates the ticket for the departure along the given route
     *
     * @param departure    the departure for which the ticket is created
     * @param userRouteDto the user route
     * @return the ticket
     */
    @Override
    public TicketDto createTicketForDepartureAlongRoute(Departure departure, UserRouteDto userRouteDto) {
        TicketDto ticketDto = new TicketDto();

        ticketDto.setDeparture(departureMapper.departureToDepartureDto(departure));
        ticketDto.setDepartureStation(userRouteDto.getDepartureStation());
        ticketDto.setArrivalStation(userRouteDto.getArrivalStation());

        setTicketDepartureAndArrivalDates(ticketDto);
        setTicketRoutePricePerDeparture(ticketDto, departure);

        if (ticketDto.getDeparture().getRoute().getType().equals("Региональные линии")) {
            orderService.setTicketFinalPrice(ticketDto);
        }

        return ticketDto;
    }

    /**
     * Sets departure and arrival dates of the generated ticket
     *
     * @param ticketDto the generated ticket
     */
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

    /**
     * Sets the route price for the generated ticket
     *
     * @param ticketDto the generated ticket
     * @param departure the departure
     */
    private void setTicketRoutePricePerDeparture(TicketDto ticketDto, Departure departure) {
        RouteStation userDepartureRouteStation =
                routeStationRepository.findByRoute_IdAndStation_Title(departure.getRoute().getId(),
                        ticketDto.getDepartureStation().getTitle());
        RouteStation userArrivalRouteStation =
                routeStationRepository.findByRoute_IdAndStation_Title(departure.getRoute().getId(),
                        ticketDto.getArrivalStation().getTitle());

        List<RouteStation> routeStations = departure.getRoute().getRouteStations();

        int price = routeStations.stream()
                .filter(routeStation -> routeStation.getRouteStopNumber() >= userDepartureRouteStation.getRouteStopNumber()
                        && routeStation.getRouteStopNumber() < userArrivalRouteStation.getRouteStopNumber())
                .mapToInt(RouteStation::getPriceToNextStation)
                .sum();

        ticketDto.setRoutePrice(price);
    }

    /**
     * Checks the generated ticket for suitability for the given route
     *
     * @param ticketDto    the generated ticket
     * @param userRouteDto the user route
     * @return true - if ticket is suitable, false - if ticket is not suitable
     */
    private boolean isTicketSuitableForUserRoute(TicketDto ticketDto, UserRouteDto userRouteDto) {
        if (userRouteDto.getDepartureDate() == null && userRouteDto.getDepartureTime() == null) {
            LocalDateTime userDepartureDateTime = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);

            return ticketDto.getDepartureDate().isAfter(userDepartureDateTime);
        } else {

            return isTicketDepartureDateAfterUserDepartureDate(ticketDto, userRouteDto);
        }
    }

    /**
     * Checks if the departure date of the generated ticket is later than the departure date of the user route
     *
     * @param ticketDto    the generated ticket
     * @param userRouteDto the user route
     * @return true - if the ticket departure date is later than the user route departure date,
     * false - if the ticket departure date is earlier than the user route departure date
     */
    private boolean isTicketDepartureDateAfterUserDepartureDate(TicketDto ticketDto, UserRouteDto userRouteDto) {
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
}
