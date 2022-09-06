package com.ironhack.ironbank.model.account;

import com.ironhack.ironbank.dto.CurrentSavingsAccountDTO;
import com.ironhack.ironbank.interfaces.InterestRate;
import com.ironhack.ironbank.interfaces.PenaltyFee;
import com.ironhack.ironbank.model.Money;
import com.ironhack.ironbank.utils.UtilsService;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "current_savings_accounts")
public class CurrentSavingsAccount extends CurrentAccount implements PenaltyFee, InterestRate {

    private static final BigDecimal DEFAULT_INTEREST_RATE = new BigDecimal("0.0025");
    private static final BigDecimal MAX_INTEREST_RATE = new BigDecimal("0.5");
    @Embedded
    private static final Money DEFAULT_MINIMUM_BALANCE = new Money(new BigDecimal("1000"));
    @Embedded
    private static final Money MIN_MINIMUM_BALANCE = new Money(new BigDecimal("100"));

    @NotNull
    @NotNull
    @Embedded
    @AttributeOverrides(
            {
                    @AttributeOverride(name = "currency", column = @Column(name = "minimum_balance_currency")),
                    @AttributeOverride(name = "amount", column = @Column(name = "minimum_balance_amount")),
            }
    )
    private Money minimumBalance;

    @NotNull
    @Column(name = "interest_rate")
    @Max(1)
    @Min(0)
    private BigDecimal interestRate;

    @Column(name = "interest_rate_date")
    private Instant interestRateDate;

    @Override
    public void applyPenaltyFee() {
        BigDecimal b1 = getBalance().getAmount();
        BigDecimal b2 = getMinimumBalance().getAmount();

        if (b1.compareTo(b2) != 0 || b1.compareTo(b2) != 1) {
            var newAmount = getBalance().decreaseAmount(PENALTY_FEE);
            setBalance(new Money(newAmount));
        }
    }

    @Override
    public void applyInterestRate() {
        var diffCreationDate = UtilsService.getDiffYears(getCreationDate());
        if (diffCreationDate >= 1) addInterestRateToBalance(diffCreationDate);
        else {
            var diffInterestRateDate = UtilsService.getDiffYears(getInterestRateDate());
            if (diffInterestRateDate >= 1) addInterestRateToBalance(diffInterestRateDate);
        }
    }

    private void setInterestRateDate() {
        this.interestRateDate = Instant.now();
    }

    private void addInterestRateToBalance(Integer diffDate) {
        for (int i = 0; i < diffDate; i++) {
            var amountObtained = getBalance().getAmount().multiply(getInterestRate());
            var newAmount = getBalance().increaseAmount(amountObtained);
            setBalance(new Money(newAmount));
        }
        setInterestRateDate();
    }

    public static CurrentSavingsAccount fromDTO(CurrentSavingsAccountDTO currentSavingsAccountDTO) {

        // From Account model
        var account = Account.fromDTO(currentSavingsAccountDTO);
        var currentSavingsAccount = new CurrentSavingsAccount();
        currentSavingsAccount.setIban(account.getIban());
        currentSavingsAccount.setBalance(account.getBalance());
        currentSavingsAccount.setPrimaryOwner(account.getPrimaryOwner());
        currentSavingsAccount.setSecondaryOwner(account.getSecondaryOwner());

        // From Current Account model
        currentSavingsAccount.setSecretKey(currentSavingsAccountDTO.getSecretKey());
        currentSavingsAccount.setCreationDate(currentSavingsAccountDTO.getCreationDate());
        currentSavingsAccount.setStatus(currentSavingsAccountDTO.getStatus());

        // From Current Savings Account model
        var minimumBalance = Money.fromDTO(currentSavingsAccountDTO.getMinimumBalance());
        currentSavingsAccount.setMinimumBalance(minimumBalance);
        currentSavingsAccount.setInterestRate(currentSavingsAccountDTO.getInterestRate());
        currentSavingsAccount.setInterestRateDate(currentSavingsAccountDTO.getInterestRateDate());

        return currentSavingsAccount;
    }
}
