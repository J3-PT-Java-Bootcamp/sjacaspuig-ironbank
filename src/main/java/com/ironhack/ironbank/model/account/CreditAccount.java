package com.ironhack.ironbank.model.account;

import com.ironhack.ironbank.dto.CreditAccountDTO;
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
@NoArgsConstructor
@Table(name = "credit_accounts")
public class CreditAccount extends Account implements InterestRate {

    @Embedded
    private static final Money DEFAULT_CREDIT_LIMIT = new Money(new BigDecimal("100"));
    @Embedded
    private static final Money MAX_CREDIT_LIMIT = new Money(new BigDecimal("100000"));
    private static final BigDecimal DEFAULT_INTEREST_RATE = new BigDecimal("0.2");
    private static final BigDecimal MIN_INTEREST_RATE = new BigDecimal("0.1");

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

    @Override
    public void applyInterestRate() {
        var diffInterestRateDate = DateService.getDiffMonths(getInterestRateDate());
        if (diffInterestRateDate >= 1) addInterestRateToBalance(diffInterestRateDate);
    }

    private void setInterestRateDate() {
        this.interestRateDate = Instant.now();
    }

    private void addInterestRateToBalance(Integer diffDate) {
        for (int i = 0; i < diffDate; i++) {
            var interestRatePerMonth = getInterestRate().divide(new BigDecimal("12"));
            var amountObtained = getBalance().getAmount().multiply(interestRatePerMonth);
            var newAmount = getBalance().increaseAmount(amountObtained);
            setBalance(new Money(newAmount));
        }
        setInterestRateDate();
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
        var money = Money.fromDTO(creditAccountDTO.getCreditLimit());
        creditAccount.setCreditLimit(money);
        creditAccount.setInterestRate(creditAccountDTO.getInterestRate());
        if(creditAccountDTO.getInterestRateDate() != null) {
            var interestRateDate = DateService.parseDate(creditAccountDTO.getInterestRateDate());
            creditAccount.setInterestRateDate(interestRateDate);
        }

        return creditAccount;
    }
}
