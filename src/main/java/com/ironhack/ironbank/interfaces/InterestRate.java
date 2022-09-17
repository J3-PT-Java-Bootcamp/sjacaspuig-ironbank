package com.ironhack.ironbank.interfaces;

import com.ironhack.ironbank.model.MyDecimal;

import java.math.BigDecimal;

public interface InterestRate {
    default boolean isInterestRateOverMin(BigDecimal interestRate, BigDecimal minimumInterestRate) {
        var isInterestRateValid = interestRate.compareTo(minimumInterestRate) >= 0;
        return isInterestRateValid;
    }

    default boolean isInterestRateUnderMax(MyDecimal interestRate, MyDecimal maximumInterestRate) {
        var isInterestRateValid = interestRate.compareTo(maximumInterestRate) <= 0;
        return isInterestRateValid;
    }
}
