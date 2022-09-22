package com.ironhack.ironbank.dto.response;

import com.ironhack.ironbank.dto.AccountHolderDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class AccountHolderCreateResponse {

    private int Status;
    private AccountHolderDTO accountHolder;
    private String message;
}
