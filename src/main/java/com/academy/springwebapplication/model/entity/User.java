package com.academy.springwebapplication.model.entity;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@EqualsAndHashCode(of = {"id", "username", "role", "accountNonLocked", "userInformation"})
@ToString(of = {"id", "username", "role", "accountNonLocked", "userInformation"})
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String username;

    @Column
    private String password;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "role_id")
    private Role role;

    @Column(name = "account_non_locked")
    private boolean accountNonLocked;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private UserInformation userInformation;
}
