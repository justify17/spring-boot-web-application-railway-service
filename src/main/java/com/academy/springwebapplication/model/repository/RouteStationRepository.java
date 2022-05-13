package com.academy.springwebapplication.model.repository;

import com.academy.springwebapplication.model.entity.RouteStation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RouteStationRepository extends JpaRepository<RouteStation,Integer> {
    RouteStation findByRoute_IdAndStation_Title(Integer routeId, String stationTitle);
}
