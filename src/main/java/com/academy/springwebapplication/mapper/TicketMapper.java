package com.academy.springwebapplication.mapper;

import com.academy.springwebapplication.dto.TicketDto;
import com.academy.springwebapplication.mapper.decorator.TicketMapperDecorator;
import com.academy.springwebapplication.model.entity.Ticket;
import org.mapstruct.*;

@Mapper(componentModel = "spring", uses = {DepartureMapper.class})
@DecoratedWith(TicketMapperDecorator.class)
public interface TicketMapper {

    @Mappings({
            @Mapping(source = "ticketDto.departureStation", target = "userDepartureStation"),
            @Mapping(source = "ticketDto.departureDate", target = "userDepartureDate"),
            @Mapping(source = "ticketDto.arrivalStation", target = "userArrivalStation"),
            @Mapping(source = "ticketDto.arrivalDate", target = "userArrivalDate")
    })
    Ticket ticketDtoToTicket(TicketDto ticketDto);

    @InheritInverseConfiguration
    @Mapping(source = "ticket.user.username", target = "username")
    TicketDto ticketToTicketDto(Ticket ticket);
}
