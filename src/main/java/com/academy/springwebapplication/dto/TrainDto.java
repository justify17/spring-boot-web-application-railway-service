package com.academy.springwebapplication.dto;

import lombok.Data;

import java.util.List;

@Data
public class TrainDto {
    private Integer id;
    private String number;
    private String type;
    private List<CarriageDto> carriages;
}
