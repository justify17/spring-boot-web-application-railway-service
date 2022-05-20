package com.academy.springwebapplication.model.repository;

import com.academy.springwebapplication.model.entity.Station;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StationRepository extends JpaRepository<Station, Integer> {
    Station findByTitle(String title);
}
