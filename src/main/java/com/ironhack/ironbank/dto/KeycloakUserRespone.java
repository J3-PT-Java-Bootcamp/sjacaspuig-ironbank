package com.ironhack.ironbank.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class KeycloakUserRespone {

    private int status;
    private UserSecurityDTO user;
}
