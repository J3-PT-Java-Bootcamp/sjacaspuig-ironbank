package com.ironhack.ironbank.interfaces;

import java.math.BigDecimal;

public interface InterestRate {
    void applyInterestRate();
    default boolean isInterestRateOverMin(BigDecimal interestRate, BigDecimal minimumInterestRate) {
        var isInterestRateValid = interestRate.compareTo(minimumInterestRate) >= 0;
        return isInterestRateValid;
    }

    default boolean isInterestRateUnderMax(BigDecimal interestRate, BigDecimal maximumInterestRate) {
        var isInterestRateValid = interestRate.compareTo(maximumInterestRate) <= 0;
        return isInterestRateValid;
    }
}
