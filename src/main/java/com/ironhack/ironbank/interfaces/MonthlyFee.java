package com.ironhack.ironbank.interfaces;

import com.ironhack.ironbank.constants.AccountConstants;
import com.ironhack.ironbank.model.Money;

public interface MonthlyFee {

    default Money chargeMonthlyFee(Money balance, Money monthlyFee) {
        var newAmount = balance.decreaseAmount(monthlyFee);
        return new Money(newAmount);
    }
}
