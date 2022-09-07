package com.ironhack.ironbank.dto;

import com.ironhack.ironbank.enums.AccountStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public abstract class CurrentAccountDTO extends AccountDTO {

    private String secretKey;
    private Date creationDate;
    private AccountStatus status;
}
