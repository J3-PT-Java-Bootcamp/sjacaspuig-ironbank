package com.ironhack.ironbank.model.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@MappedSuperclass
public class UserSecurity extends User {

    @NotNull
    private String username;

    @NotNull
    private String email;
}
