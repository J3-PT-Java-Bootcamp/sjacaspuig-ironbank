package com.ironhack.ironbank.model.account;

import com.ironhack.ironbank.interfaces.PenaltyFee;
import com.ironhack.ironbank.model.Money;
import com.ironhack.ironbank.model.user.AccountHolder;
import com.ironhack.ironbank.utils.UtilsService;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "current_checking_accounts")
public class CurrentCheckingAccount extends CurrentAccount implements PenaltyFee {

    @Embedded
    private static final Money MINIMUM_BALANCE = new Money(new BigDecimal("250"));
    @Embedded
    private static final Money MONTHLY_MAINTENANCE_FEE = new Money(new BigDecimal("12"));
    private static final int MIN_AGE = 24;

    private boolean isOwnerAllowedToCreate(AccountHolder accountHolder) {
        return UtilsService.getDiffYears(accountHolder.getBirthDate()) >= MIN_AGE;
    }

    @Override
    public void applyPenaltyFee() {
        BigDecimal b1 = getBalance().getAmount();
        BigDecimal b2 = MINIMUM_BALANCE.getAmount();

        if (b1.compareTo(b2) != 0 || b1.compareTo(b2) != 1) {
            var newAmount = getBalance().decreaseAmount(PENALTY_FEE);
            setBalance(new Money(newAmount));
        }
    }
}
