package com.academy.springwebapplication.service.impl;

import com.academy.springwebapplication.exception.EntityByTitleNotFoundException;
import com.academy.springwebapplication.model.entity.Station;
import com.academy.springwebapplication.model.repository.StationRepository;
import com.academy.springwebapplication.service.StationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StationServiceImpl implements StationService {
    private final StationRepository stationRepository;

    @Override
    public void checkIfStationTitleIsValid(String stationTitle){
        Station station = stationRepository.findByTitle(stationTitle);

        if(station == null){

            throw new EntityByTitleNotFoundException(stationTitle);
        }
    }
}
