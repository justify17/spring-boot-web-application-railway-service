package com.academy.springwebapplication.model.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
@Table(name = "trains")
public class Train {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String number;

    @OneToMany(mappedBy = "train", cascade = CascadeType.ALL)
    private Set<Departure> departures;
}
