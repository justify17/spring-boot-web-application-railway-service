package com.academy.springwebapplication.service;

import com.academy.springwebapplication.model.entity.Departure;
import com.academy.springwebapplication.model.repository.DepartureRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartureServiceImpl implements DepartureService{
    private final DepartureRepository departureRepository;

    @Override
    public List<Departure> getDepartures() {
        return departureRepository.findAll();
    }
}
