package com.ironhack.ironbank.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserSecurityDTO extends UserDTO {

    private String username;
    private String email;
}
