package com.academy.springwebapplication.model.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@EqualsAndHashCode(of = {"id", "name"})
@ToString(of = {"id", "name"})
@NoArgsConstructor
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

    public Role(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
}
