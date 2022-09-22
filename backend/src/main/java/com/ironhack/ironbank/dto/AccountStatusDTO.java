package com.ironhack.ironbank.dto;

import com.ironhack.ironbank.enums.AccountStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AccountStatusDTO {

    private String iban;
    private AccountStatus status;
}
