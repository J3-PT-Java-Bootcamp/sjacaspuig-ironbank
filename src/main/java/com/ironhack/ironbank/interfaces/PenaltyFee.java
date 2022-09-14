package com.ironhack.ironbank.interfaces;

import com.ironhack.ironbank.constants.AccountConstants;
import com.ironhack.ironbank.model.Money;

public interface PenaltyFee {
    default Money chargePenaltyFee(Money balance, Money penaltyFee) {
        var newAmount = balance.decreaseAmount(penaltyFee);
        return new Money(newAmount);
    }
}
