package com.academy.springwebapplication.dto;

import lombok.Data;

@Data
public class SeatDto {
    private int carriageNumber;
    private int number;
    private boolean free;
}
