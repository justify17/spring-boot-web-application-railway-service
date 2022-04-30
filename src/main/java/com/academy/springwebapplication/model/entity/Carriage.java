package com.academy.springwebapplication.model.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "carriages")
public class Carriage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String type;

    @Column(name = "comfort_level")
    private String comfortLevel;

    @Column
    private Integer seats;
}
