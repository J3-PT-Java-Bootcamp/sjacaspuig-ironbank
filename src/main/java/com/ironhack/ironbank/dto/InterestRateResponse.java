package com.ironhack.ironbank.dto;

import com.ironhack.ironbank.model.Money;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class InterestRateResponse {
    private BigDecimal interestRateApplied;
    private Money earnings;
    private int timesApplied;
}
