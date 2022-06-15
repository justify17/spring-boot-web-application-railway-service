package com.academy.springwebapplication.mapper;

import com.academy.springwebapplication.dto.DepartureDto;
import com.academy.springwebapplication.dto.RouteStationDto;
import com.academy.springwebapplication.dto.StationDto;
import com.academy.springwebapplication.model.entity.Departure;
import com.academy.springwebapplication.model.entity.RouteStation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public abstract class DepartureMapperDecorator implements DepartureMapper {
    @Autowired
    @Qualifier("delegate")
    private DepartureMapper delegate;

    @Override
    public DepartureDto departureToDepartureDto(Departure departure) {
        DepartureDto departureDto = delegate.departureToDepartureDto(departure);
        departureDto.getRoute().setRouteStations(getRouteStations(departure));

        return departureDto;
    }

    private List<RouteStationDto> getRouteStations(Departure departure) {
        List<RouteStationDto> routeStationsDto = new ArrayList<>();

        List<RouteStation> routeStations = departure.getRoute().getRouteStations();

        LocalDateTime arrivalDateAtNextStation = departure.getDepartureDate();
        for (RouteStation routeStation : routeStations) {
            RouteStationDto routeStationDto = new RouteStationDto();

            StationDto stationDto = new StationDto();
            stationDto.setTitle(routeStation.getStation().getTitle());
            routeStationDto.setStation(stationDto);

            routeStationDto.setArrivalDate(arrivalDateAtNextStation);
            routeStationDto.setDepartureDate(routeStationDto.getArrivalDate().plusMinutes(routeStation.getStopMinutes()));

            routeStationsDto.add(routeStationDto);

            arrivalDateAtNextStation = routeStationDto.getDepartureDate().plusMinutes(routeStation.getMinutesToNextStation());
        }

        routeStationsDto.get(0).setArrivalDate(null);
        routeStationsDto.get(routeStationsDto.size() - 1).setDepartureDate(null);

        return routeStationsDto;
    }
}
