package com.academy.springwebapplication.dto;

import com.academy.springwebapplication.annotation.StationExists;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StationDto {

    @NotBlank(message = "The field station must not be empty")
    @StationExists
    private String title;
}
