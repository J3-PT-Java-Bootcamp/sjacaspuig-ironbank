package com.ironhack.ironbank.model.account;

import com.ironhack.ironbank.constants.AccountConstants;
import com.ironhack.ironbank.dto.CreditAccountDTO;
import com.ironhack.ironbank.enums.AccountType;
import com.ironhack.ironbank.interfaces.InterestRate;
import com.ironhack.ironbank.model.Money;
import com.ironhack.ironbank.model.user.AccountHolder;
import com.ironhack.ironbank.utils.DateService;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Getter
@Setter
@Table(name = "credit_accounts")
public class CreditAccount extends Account implements InterestRate {

    private static final Money DEFAULT_CREDIT_LIMIT = AccountConstants.CREDIT_ACCOUNT_DEFAULT_CREDIT_LIMIT;
    private static final Money MAX_CREDIT_LIMIT = AccountConstants.CREDIT_ACCOUNT_MAX_CREDIT_LIMIT;
    private static final BigDecimal DEFAULT_INTEREST_RATE = AccountConstants.CREDIT_ACCOUNT_DEFAULT_INTEREST_RATE;
    private static final BigDecimal MIN_INTEREST_RATE = AccountConstants.CREDIT_ACCOUNT_MIN_INTEREST_RATE;

    @NotNull
    @Embedded
    @AttributeOverrides(
            {
                    @AttributeOverride(name = "currency", column = @Column(name = "credit_limit_currency")),
                    @AttributeOverride(name = "amount", column = @Column(name = "credit_limit_amount")),
            }
    )
    private Money creditLimit;

    @NotNull
    @Column(name = "interest_rate")
    @Max(1)
    @Min(0)
    private BigDecimal interestRate;

    @CreationTimestamp()
    @Column(name = "interest_rate_date")
    private Instant interestRateDate;

    public CreditAccount() {
        setCreditLimit(DEFAULT_CREDIT_LIMIT);
        setInterestRate(DEFAULT_INTEREST_RATE);
        setAccountType(AccountType.CREDIT);
    }

    public void setCreditLimit(Money creditLimit) {
        if (creditLimit.getAmount().compareTo(MAX_CREDIT_LIMIT.getAmount()) > 0) {
            throw new IllegalArgumentException("Credit limit cannot be greater than " + MAX_CREDIT_LIMIT);
        }
        this.creditLimit = creditLimit;
    }

    public void setBalance(Money balance) {
        if (balance.getAmount().compareTo(creditLimit.getAmount()) > 0) {
            throw new IllegalArgumentException("Balance cannot be greater than the maximum credit limit.");
        } else {
            super.setBalance(balance, AccountConstants.GLOBAL_MINIMUM_BALANCE);
        }
    }

    public void setInterestRate(BigDecimal interestRate) {
        // Check if interest rate is null and if it is, set it to the default value
        if (interestRate == null) {
            this.interestRate = DEFAULT_INTEREST_RATE;
        } else {
            // Check if interest rate is less than the minimum allowed
            if (isInterestRateValid(interestRate, MIN_INTEREST_RATE)) {
                this.interestRate = interestRate;
            } else {
                throw new IllegalArgumentException("Interest rate cannot be less than " + MIN_INTEREST_RATE);
            }
        }
    }

    @Override
    public void applyInterestRate() {
        var diffInterestRateDate = DateService.getDiffMonths(getInterestRateDate());
        if (diffInterestRateDate >= 1) addInterestRateToBalance(diffInterestRateDate);
    }

    private void addInterestRateToBalance(Integer diffDate) {

        for (int i = 0; i < diffDate; i++) {
            var interestRatePerMonth = getInterestRate().divide(new BigDecimal("12"));
            var amountObtained = getBalance().getAmount().multiply(interestRatePerMonth);
            var newAmount = getBalance().increaseAmount(amountObtained);
            setBalance(new Money(newAmount));
        }

        this.interestRateDate = Instant.now();
    }

    public static CreditAccount fromDTO(CreditAccountDTO creditAccountDTO, AccountHolder primaryOwner, AccountHolder secondaryOwner) {

        // From Account model
        var account = Account.fromDTO(creditAccountDTO, primaryOwner, secondaryOwner);
        var creditAccount = new CreditAccount();
        creditAccount.setIban(account.getIban());
        creditAccount.setBalance(account.getBalance());
        creditAccount.setPrimaryOwner(account.getPrimaryOwner());
        creditAccount.setSecondaryOwner(account.getSecondaryOwner());

        // From Credit Account model
        if (creditAccountDTO.getCreditLimit() != null) {
            var money = Money.fromDTO(creditAccountDTO.getCreditLimit());
            creditAccount.setCreditLimit(money);
        }
        creditAccount.setInterestRate(creditAccountDTO.getInterestRate());
        if(creditAccountDTO.getInterestRateDate() != null) {
            var interestRateDate = DateService.parseDate(creditAccountDTO.getInterestRateDate());
            creditAccount.setInterestRateDate(interestRateDate);
        }

        return creditAccount;
    }
}
