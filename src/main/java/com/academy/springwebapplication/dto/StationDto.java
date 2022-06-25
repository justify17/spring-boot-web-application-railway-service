package com.academy.springwebapplication.dto;

import com.academy.springwebapplication.annotation.StationExists;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StationDto {

    @NotBlank(message = "The field station must not be empty")
    @StationExists
    private String title;
}
