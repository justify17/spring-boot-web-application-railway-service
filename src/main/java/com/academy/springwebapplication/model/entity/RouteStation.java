package com.academy.springwebapplication.model.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "routes_stations")
public class RouteStation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "route_id")
    private Route route;

    @ManyToOne
    @JoinColumn(name = "station_id")
    private Station station;

    @Column(name = "route_stop_number")
    private Integer routeStopNumber;

    @Column(name = "price_to_next_station")
    private Integer priceToNextStation;

    @Column(name = "minutes_to_next_station")
    private Integer minutesToNextStation;

    @Column(name = "stop_minutes")
    private Integer stopMinutes;
}
