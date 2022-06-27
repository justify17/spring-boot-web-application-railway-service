package com.academy.springwebapplication.util;

import com.academy.springwebapplication.dto.*;
import com.academy.springwebapplication.model.entity.Departure;
import com.academy.springwebapplication.model.entity.Route;
import com.academy.springwebapplication.model.entity.RouteStation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class TestObjectFactory {

    public UserRegistrationDto getValidUserRegistrationDto() {
        String username = "newUser";
        String password = "newPassword";

        return UserRegistrationDto.builder()
                .username(username)
                .password(password)
                .build();
    }

    public UserRegistrationDto getInvalidUserRegistrationDto() {
        String username = "@@a";
        String password = "   __";

        return UserRegistrationDto.builder()
                .username(username)
                .password(password)
                .build();
    }

    public StationDto getValidStationDto() {
        String title = "Гомель";

        return StationDto.builder()
                .title(title)
                .build();
    }

    public StationDto getInvalidStationDto() {
        String title = "Москва123";

        return StationDto.builder()
                .title(title)
                .build();
    }

    public DepartureDto getDepartureDto() {

        return DepartureDto.builder()
                .id(1)
                .train(getTrainDto())
                .route(getRouteDto())
                .departureDate(LocalDateTime.now().plusDays(1))
                .build();
    }

    public DepartureDto getValidNewDepartureDto() {
        TrainDto validTrain = TrainDto.builder()
                .id(3)
                .build();

        RouteDto validRoute = RouteDto.builder()
                .id(3)
                .build();

        return DepartureDto.builder()
                .train(validTrain)
                .route(validRoute)
                .departureDate(LocalDateTime.now().plusMonths(1))
                .build();
    }

    public DepartureDto getInvalidNewDepartureDto() {
        TrainDto invalidTrain = TrainDto.builder()
                .id(-1)
                .build();

        RouteDto invalidRoute = RouteDto.builder()
                .id(-5)
                .build();

        return DepartureDto.builder()
                .train(invalidTrain)
                .route(invalidRoute)
                .departureDate(LocalDateTime.now().minusMonths(1))
                .build();
    }

    private TrainDto getTrainDto() {

        return TrainDto.builder()
                .id(1)
                .type("regional")
                .number("735")
                .carriages(List.of(getCarriageDto()))
                .build();
    }

    private CarriageDto getCarriageDto() {

        return CarriageDto.builder()
                .comfortLevel("LUX")
                .numberOfSeats(30)
                .number(1)
                .build();
    }

    private RouteDto getRouteDto() {

        return RouteDto.builder()
                .id(1)
                .type("local")
                .routeStations(List.of(getRouteStationDto(), getRouteStationDto()))
                .build();
    }

    public RouteStationDto getRouteStationDto() {

        return RouteStationDto.builder()
                .station(getValidStationDto())
                .routeStopNumber(1)
                .departureDate(LocalDateTime.now())
                .arrivalDate(LocalDateTime.now().plusMinutes(60))
                .build();
    }

    public UserRouteDto getValidUserRouteDto() {

        return UserRouteDto.builder()
                .arrivalStation(getValidStationDto())
                .departureStation(getValidStationDto())
                .departureDate(LocalDate.now().plusDays(1))
                .departureTime(LocalTime.now())
                .build();
    }

    public UserRouteDto getInvalidUserRouteDto() {

        return UserRouteDto.builder()
                .arrivalStation(getInvalidStationDto())
                .departureStation(getInvalidStationDto())
                .departureDate(LocalDate.now().minusDays(1))
                .departureTime(LocalTime.now())
                .build();
    }

    public TicketDto getTicketDto() {

        return TicketDto.builder()
                .id(1)
                .departure(getDepartureDto())
                .username("user")
                .departureStation(getValidStationDto())
                .departureDate(LocalDateTime.now().plusDays(1))
                .arrivalStation(getValidStationDto())
                .arrivalDate(LocalDateTime.now().plusDays(2))
                .routePrice(1500)
                .build();
    }

    public CreditCardDto getValidCreditCardDto() {
        String validNumber = "2762 7326 8592 3194";
        int validMonthOfCardExpiration = 10;
        int validYearOfCardExpiration = 25;

        return CreditCardDto.builder()
                .number(validNumber)
                .monthOfCardExpiration(validMonthOfCardExpiration)
                .yearOfCardExpiration(validYearOfCardExpiration)
                .build();
    }

    public CreditCardDto getInvalidCreditCardDto() {
        String invalidNumber = "2762 73     26 8592    1 ";
        int invalidMonthOfCardExpiration = 13;
        int invalidYearOfCardExpiration = 20;

        return CreditCardDto.builder()
                .number(invalidNumber)
                .monthOfCardExpiration(invalidMonthOfCardExpiration)
                .yearOfCardExpiration(invalidYearOfCardExpiration)
                .build();
    }

    public Departure getDeparture() {
        Departure departure = new Departure();
        departure.setId(1);
        departure.setRoute(getRoute());

        return departure;
    }

    public Route getRoute() {
        Route route = new Route();
        route.setId(1);
        route.setRouteStations(List.of(new RouteStation(), new RouteStation()));

        return route;
    }

}

