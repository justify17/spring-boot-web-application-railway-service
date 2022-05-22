package com.academy.springwebapplication.model.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "tickets")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "departure_id")
    private Departure departure;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_departure_station_id")
    private Station userDepartureStation;

    @Column(name = "user_departure_date")
    private LocalDateTime userDepartureDate;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "user_arrival_station_id")
    private Station userArrivalStation;

    @Column(name = "user_arrival_date")
    private LocalDateTime userArrivalDate;

    @Column(name = "carriage_number")
    private Integer carriageNumber;

    @Column(name = "seat_number")
    private Integer seatNumber;

    @Column
    private Integer price;
}
