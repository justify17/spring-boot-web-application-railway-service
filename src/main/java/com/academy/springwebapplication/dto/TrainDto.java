package com.academy.springwebapplication.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Positive;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TrainDto {

    @Positive(message = "The field train id must be positive")
    private int id;

    private String number;
    private String type;
    private List<CarriageDto> carriages;
}
