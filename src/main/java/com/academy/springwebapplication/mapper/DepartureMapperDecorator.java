package com.academy.springwebapplication.mapper;

import com.academy.springwebapplication.dto.DepartureDto;
import com.academy.springwebapplication.dto.StationDto;
import com.academy.springwebapplication.dto.StationSchedule;
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
        departureDto.setStationSchedules(getStationSchedules(departure));

        return departureDto;
    }

    private List<StationSchedule> getStationSchedules(Departure departure) {
        List<StationSchedule> stationSchedules = new ArrayList<>();

        List<RouteStation> routeStations = departure.getRoute().getRouteStations();

        LocalDateTime arrivalDateAtNextStation = departure.getDepartureDate();
        for (RouteStation routeStation : routeStations) {
            StationSchedule stationSchedule = new StationSchedule();

            StationDto stationDto = new StationDto();
            stationDto.setTitle(routeStation.getStation().getTitle());
            stationSchedule.setStation(stationDto);

            stationSchedule.setArrivalDate(arrivalDateAtNextStation);
            stationSchedule.setDepartureDate(stationSchedule.getArrivalDate().plusMinutes(routeStation.getStopMinutes()));

            stationSchedules.add(stationSchedule);

            arrivalDateAtNextStation = stationSchedule.getDepartureDate().plusMinutes(routeStation.getMinutesToNextStation());
        }

        stationSchedules.get(0).setArrivalDate(null);
        stationSchedules.get(stationSchedules.size() - 1).setDepartureDate(null);

        return stationSchedules;
    }
}
