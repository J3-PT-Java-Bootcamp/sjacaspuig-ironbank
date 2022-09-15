package com.ironhack.ironbank.model.account;

import com.ironhack.ironbank.constants.AccountConstants;
import com.ironhack.ironbank.dto.CurrentSavingsAccountDTO;
import com.ironhack.ironbank.enums.AccountStatus;
import com.ironhack.ironbank.enums.AccountType;
import com.ironhack.ironbank.interfaces.InterestRate;
import com.ironhack.ironbank.interfaces.PenaltyFee;
import com.ironhack.ironbank.model.Money;
import com.ironhack.ironbank.model.user.AccountHolder;
import com.ironhack.ironbank.utils.DateService;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Getter
@Setter
@Table(name = "current_savings_accounts")
public class CurrentSavingsAccount extends CurrentAccount implements InterestRate {

    private static final BigDecimal DEFAULT_INTEREST_RATE = AccountConstants.SAVINGS_ACCOUNT_DEFAULT_INTEREST_RATE;
    private static final BigDecimal MAX_INTEREST_RATE = AccountConstants.SAVINGS_ACCOUNT_MAX_INTEREST_RATE;
    public static final Money DEFAULT_MINIMUM_BALANCE = AccountConstants.SAVINGS_ACCOUNT_DEFAULT_MINIMUM_BALANCE;
    private static final Money MIN_MINIMUM_BALANCE = AccountConstants.SAVINGS_ACCOUNT_MIN_MINIMUM_BALANCE;

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
    @DecimalMax(value = "0.5", message = "Interest rate must be less than 0.5")
    @Min(0)
    private BigDecimal interestRate;

    @CreationTimestamp()
    @Column(name = "interest_rate_date")
    private Instant interestRateDate;

    public CurrentSavingsAccount() {
        super.setStatus(AccountStatus.ACTIVE);
        setMinimumBalance(DEFAULT_MINIMUM_BALANCE);
        setInterestRate(DEFAULT_INTEREST_RATE);
        setAccountType(AccountType.SAVINGS);
    }

    public void setMinimumBalance (Money minimumBalance) {
        if (minimumBalance.getAmount().compareTo(MIN_MINIMUM_BALANCE.getAmount()) >= 0) {
            this.minimumBalance = minimumBalance;
        } else {
            throw new IllegalArgumentException("Minimum balance must be greater than " + MIN_MINIMUM_BALANCE);
        }
    }

    @Override
    public void setBalance(@NotNull Money balance) {
        if (getStatus() == AccountStatus.ACTIVE) {
            super.setBalance(balance);
        } else {
            throw new IllegalArgumentException("Account status must be active to set the balance");
        }
    }

    @Override
    public void applyInterestRate() {
        var diffCreationDate = DateService.getDiffYears(getCreationDate());
        if (diffCreationDate >= 1) addInterestRateToBalance(diffCreationDate);
        else {
            var diffInterestRateDate = DateService.getDiffYears(getInterestRateDate());
            if (diffInterestRateDate >= 1) addInterestRateToBalance(diffInterestRateDate);
        }
    }

    private void addInterestRateToBalance(Integer diffDate) {
        for (int i = 0; i < diffDate; i++) {
            var amountObtained = getBalance().getAmount().multiply(getInterestRate());
            var newAmount = getBalance().increaseAmount(amountObtained);
            setBalance(new Money(newAmount));
        }
        this.interestRateDate = Instant.now();
    }

    public static CurrentSavingsAccount fromDTO(CurrentSavingsAccountDTO currentSavingsAccountDTO, AccountHolder primaryOwner, AccountHolder secondaryOwner) {

        // From Account model
        var account = Account.fromDTO(currentSavingsAccountDTO, primaryOwner, secondaryOwner);
        var currentSavingsAccount = new CurrentSavingsAccount();
        currentSavingsAccount.setIban(account.getIban());
        currentSavingsAccount.setBalance(account.getBalance());
        currentSavingsAccount.setPrimaryOwner(account.getPrimaryOwner());
        currentSavingsAccount.setSecondaryOwner(account.getSecondaryOwner());

        // From Current Account model
        currentSavingsAccount.setSecretKey(currentSavingsAccountDTO.getSecretKey());
        if (currentSavingsAccountDTO.getCreationDate() != null) {
            var creationDate = DateService.parseDate(currentSavingsAccountDTO.getCreationDate());
            currentSavingsAccount.setCreationDate(creationDate);
        }

        if (currentSavingsAccountDTO.getStatus() != null) {
            currentSavingsAccount.setStatus(currentSavingsAccountDTO.getStatus());
        }

        // From Current Savings Account model
        if (currentSavingsAccountDTO.getMinimumBalance() != null) {
            var minimumBalance = Money.fromDTO(currentSavingsAccountDTO.getMinimumBalance());
            currentSavingsAccount.setMinimumBalance(minimumBalance);
        }
        if (currentSavingsAccountDTO.getInterestRate() != null) {
            currentSavingsAccount.setInterestRate(currentSavingsAccountDTO.getInterestRate());
        }
        if(currentSavingsAccountDTO.getInterestRateDate() != null) {
            var interestRateDate = DateService.parseDate(currentSavingsAccountDTO.getInterestRateDate());
            currentSavingsAccount.setInterestRateDate(interestRateDate);
        }

        return currentSavingsAccount;
    }
}
