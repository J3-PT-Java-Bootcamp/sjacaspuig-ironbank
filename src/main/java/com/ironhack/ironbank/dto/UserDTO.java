package com.ironhack.ironbank.dto;

import lombok.Getter;
import lombok.Setter;



@Getter
@Setter
public abstract class UserDTO {

    private String id;
    private String firstName;
    private String lastName;
}
