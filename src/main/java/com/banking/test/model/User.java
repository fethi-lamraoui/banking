package com.banking.test.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"username"})
@Entity
public class User implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String firstname;

    @Column(nullable = false)
    private String lastname;

    @ManyToMany(fetch = FetchType.LAZY)
    private Set<Role> roles;

    @OneToOne(cascade=CascadeType.ALL, fetch = FetchType.LAZY)
    private Account account;

    @OneToMany(fetch = FetchType.LAZY)
    private Set<Transaction> transactions;
}
