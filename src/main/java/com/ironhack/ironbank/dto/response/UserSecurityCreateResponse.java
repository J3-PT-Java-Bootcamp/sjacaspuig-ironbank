package com.ironhack.ironbank.dto.response;

import com.ironhack.ironbank.dto.UserSecurityDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserSecurityCreateResponse {

    private int status;
    private UserSecurityDTO user;
    private String message;
}
