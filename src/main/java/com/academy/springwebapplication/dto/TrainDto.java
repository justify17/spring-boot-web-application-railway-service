package com.academy.springwebapplication.dto;

import lombok.Data;

import java.util.List;

@Data
public class TrainDto {
    private String number;
    private List<CarriageDto> carriages;
}
