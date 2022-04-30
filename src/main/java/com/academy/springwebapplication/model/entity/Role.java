package com.academy.springwebapplication.model.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY )
    private Integer id;

    @Column
    private String name;

    @OneToMany(mappedBy = "role", cascade = {CascadeType.MERGE,CascadeType.PERSIST})
    private Set<User> users;
}
