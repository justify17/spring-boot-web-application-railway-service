package com.academy.springwebapplication.mapper;

import com.academy.springwebapplication.dto.TicketDto;
import com.academy.springwebapplication.dto.UserRouteDto;
import com.academy.springwebapplication.model.entity.Departure;
import com.academy.springwebapplication.model.entity.Ticket;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {DepartureMapper.class})
public interface TicketMapper {

    @Mapping(source = "userRouteDto.departureStation", target = "departureStation")
    @Mapping(source = "userRouteDto.arrivalStation", target = "arrivalStation")
    @Mapping(source = "departure", target = "departure")
    TicketDto departureAndUserRouteDtoToTicketDto(Departure departure, UserRouteDto userRouteDto);

    @Mapping(source = "ticketDto.departure.id", target = "departure.id")
    @Mapping(source = "ticketDto.departureStation", target = "userDepartureStation")
    @Mapping(source = "ticketDto.arrivalStation", target = "userArrivalStation")
    Ticket ticketDtoToTicket(TicketDto ticketDto);
}
