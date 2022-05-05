package com.academy.springwebapplication.model.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode(of = {"id", "number"})
@ToString(of = {"id", "number"})
@NoArgsConstructor
@Entity
@Table(name = "trains")
public class Train {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String number;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "train", cascade = CascadeType.ALL)
    private Set<Departure> departures;

    @OneToMany(mappedBy = "train", cascade = CascadeType.ALL)
    private Set<TrainCarriage> carriages;
}
