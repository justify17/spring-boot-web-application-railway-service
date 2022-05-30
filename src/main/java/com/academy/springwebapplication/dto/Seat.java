package com.academy.springwebapplication.dto;

import lombok.Data;

@Data
public class Seat {
    private int carriageNumber;
    private int number;
    private boolean isFree;
}
