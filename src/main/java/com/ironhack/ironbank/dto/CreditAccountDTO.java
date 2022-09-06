package com.ironhack.ironbank.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreditAccountDTO extends AccountDTO {

    private MoneyDTO creditLimit;
    private BigDecimal interestRate;
    private Instant interestRateDate;
}
