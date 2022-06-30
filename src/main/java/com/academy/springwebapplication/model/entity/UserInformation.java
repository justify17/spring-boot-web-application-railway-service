package com.academy.springwebapplication.model.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@EqualsAndHashCode(of = {"id", "firstName", "surname", "phoneNumber"})
@ToString(of = {"id", "firstName", "surname", "phoneNumber"})
@NoArgsConstructor
@Entity
@Table(name = "user_information")
public class UserInformation {
    @Id
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
    private String phoneNumber;
}
