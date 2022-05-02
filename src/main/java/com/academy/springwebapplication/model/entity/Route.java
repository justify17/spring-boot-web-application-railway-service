package com.academy.springwebapplication.model.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
@Table(name = "routes")
public class Route {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String type;

    @OneToMany(mappedBy = "route", cascade = CascadeType.ALL)
    private Set<Departure> departures;

    @OneToMany(mappedBy = "route", cascade = CascadeType.ALL)
    private Set<RouteStation> stations;
}