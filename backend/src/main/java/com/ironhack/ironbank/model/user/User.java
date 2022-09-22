package com.ironhack.ironbank.model.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;


@Getter
@Setter
@NoArgsConstructor
@MappedSuperclass
public abstract class User {

    @Id
    private String id;

    @NotNull
    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;
}
