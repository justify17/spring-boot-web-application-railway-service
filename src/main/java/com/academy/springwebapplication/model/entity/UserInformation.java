package com.academy.springwebapplication.model.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "user_information")
public class UserInformation {
    @Id
    @Column(name = "user_id")
    private Integer id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "first_name")
    private String firstName;

    @Column
    private String surname;

    @Column(name = "phone_number")
    private Integer phoneNumber;
}
