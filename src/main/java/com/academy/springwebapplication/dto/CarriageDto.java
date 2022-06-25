package com.academy.springwebapplication.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CarriageDto {
    private String comfortLevel;
    private int numberOfSeats;
    private int number;
}
