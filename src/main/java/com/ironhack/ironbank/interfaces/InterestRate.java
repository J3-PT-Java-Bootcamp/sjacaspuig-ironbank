package com.ironhack.ironbank.interfaces;

import java.math.BigDecimal;

public interface InterestRate {
    void applyInterestRate();
    default boolean isInterestRateValid(BigDecimal interestRate, BigDecimal minimumInterestRate) {
        var isInterestRateValid = interestRate.compareTo(minimumInterestRate) >= 0;
        return isInterestRateValid;
    }
}
