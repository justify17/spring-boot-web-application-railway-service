package com.academy.springwebapplication.model.repository;

import com.academy.springwebapplication.model.entity.Departure;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DepartureRepository extends JpaRepository<Departure,Integer> {
}
