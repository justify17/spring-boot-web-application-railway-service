package com.academy.springwebapplication.dto;

import com.academy.springwebapplication.annotation.DepartureDateConstraint;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.Valid;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
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

    public UserRouteDto(StationDto departureStation, StationDto arrivalStation) {
        this.departureStation = departureStation;
        this.arrivalStation = arrivalStation;
    }
}
