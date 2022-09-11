package com.ironhack.ironbank.dto.http.requests;

import lombok.Getter;

@Getter
public class LoginRequest {

    String username;
    String password;
}