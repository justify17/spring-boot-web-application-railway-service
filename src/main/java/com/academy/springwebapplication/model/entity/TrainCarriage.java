package com.academy.springwebapplication.model.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "trains_carriages")
public class TrainCarriage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "train_id")
    private Train train;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "carriage_id")
    private Carriage carriage;

    @Column(name = "train_carriage_number")
    private Integer trainCarriageNumber;
}
