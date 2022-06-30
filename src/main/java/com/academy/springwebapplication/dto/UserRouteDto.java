package com.academy.springwebapplication.dto;

import com.academy.springwebapplication.annotation.DepartureDateConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.Valid;
import javax.validation.constraints.FutureOrPresent;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRouteDto {

    @Valid
    private StationDto departureStation;

    @Valid
    private StationDto arrivalStation;

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @FutureOrPresent(message = "Departure date cannot be past")
    @DepartureDateConstraint
    private LocalDate departureDate;

    @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
    private LocalTime departureTime;
}
