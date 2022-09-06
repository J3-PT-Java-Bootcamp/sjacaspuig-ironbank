package com.ironhack.ironbank.dto;

import com.ironhack.ironbank.enums.AccountStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public abstract class CurrentAccountDTO extends AccountDTO {

    private String secretKey;
    private Instant creationDate;
    private AccountStatus status;
}
