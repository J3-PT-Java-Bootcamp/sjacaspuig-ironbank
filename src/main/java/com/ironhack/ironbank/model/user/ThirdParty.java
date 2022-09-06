package com.ironhack.ironbank.model.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "third_parties")
public class ThirdParty extends User {

    @NotNull
    @Column(name = "hashed_key")
    private String hashedKey;
}
