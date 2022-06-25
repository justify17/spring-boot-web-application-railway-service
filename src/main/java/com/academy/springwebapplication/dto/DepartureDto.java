package com.academy.springwebapplication.dto;

import com.academy.springwebapplication.annotation.NewDepartureConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.Valid;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@NewDepartureConstraint
public class DepartureDto {
    private Integer id;

    @Valid
    private TrainDto train;

    @Valid
    private RouteDto route;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    @NotNull(message = "The field departure date cannot be null")
    @Future(message = "The field departure date cannot be past")
    private LocalDateTime departureDate;

    private int purchasedTickets;
}
