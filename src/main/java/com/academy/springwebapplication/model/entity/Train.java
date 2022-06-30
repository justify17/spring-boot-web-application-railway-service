package com.academy.springwebapplication.model.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode(of = {"id", "number", "type"})
@ToString(of = {"id", "number", "type"})
@NoArgsConstructor
@Entity
@Table(name = "trains")
public class Train {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String number;

    @Column
    private String type;

    @OneToMany(mappedBy = "train", cascade = CascadeType.ALL)
    private Set<Departure> departures;

    @OneToMany(mappedBy = "train", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<TrainCarriage> trainCarriages;
}
