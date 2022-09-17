package com.ironhack.ironbank.model.account;

import com.ironhack.ironbank.constants.AccountConstants;
import com.ironhack.ironbank.dto.CurrentCheckingAccountDTO;
import com.ironhack.ironbank.enums.AccountStatus;
import com.ironhack.ironbank.enums.AccountType;
import com.ironhack.ironbank.interfaces.MonthlyFee;
import com.ironhack.ironbank.model.Money;
import com.ironhack.ironbank.model.user.AccountHolder;
import com.ironhack.ironbank.utils.DateService;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.time.Instant;

@Entity
@Getter
@Setter
@Table(name = "current_checking_accounts")
public class CurrentCheckingAccount extends CurrentAccount implements MonthlyFee {

    public static final Money MINIMUM_BALANCE = AccountConstants.CHECKING_ACCOUNT_MINIMUM_BALANCE;
    public static final Money MONTHLY_MAINTENANCE_FEE = AccountConstants.CHECKING_ACCOUNT_MONTHLY_MAINTENANCE_FEE;
    private static final int MIN_AGE = AccountConstants.CHECKING_ACCOUNT_MIN_AGE;

    @CreationTimestamp
    @Column(name = "last_monthly_fee_date")
    private Instant lastMonthlyFeeDate;

    public CurrentCheckingAccount() {
        setAccountType(AccountType.CHECKING);
        super.setStatus(AccountStatus.ACTIVE);
    }

    private boolean isOwnerAllowedToCreate(AccountHolder accountHolder) {
        return DateService.getDiffYears(accountHolder.getBirthDate()) >= MIN_AGE;
    }

    public Instant getLastMonthlyFeeDate() {
        // How many months since the last fee was charged?
        int monthsSinceLastFee = DateService.getDiffMonths(lastMonthlyFeeDate);
        // If it's been more than a month, charge the fee
        if (monthsSinceLastFee > 1) {
            // Charge the fee for each month since the last fee was charged
            for (int i = 0; i < monthsSinceLastFee; i++) {
                setBalance(chargeMonthlyFee(getBalance(), MONTHLY_MAINTENANCE_FEE));
            }
            // Update the last fee date
            lastMonthlyFeeDate = Instant.now();
        }
        return lastMonthlyFeeDate;
    }


    @Override
    public void setPrimaryOwner(@NotNull AccountHolder primaryOwner) {
        if (isOwnerAllowedToCreate(primaryOwner)) {
            super.setPrimaryOwner(primaryOwner);
        } else {
            throw new IllegalArgumentException("Primary owner is younger than " + MIN_AGE + " years old and cannot create a checking account");
        }
    }

    @Override
    public void setBalance(@NotNull Money balance) {
        // Check if the status is active
        if (getStatus() == AccountStatus.ACTIVE) {
            super.setBalance(balance);
        } else {
            throw new IllegalArgumentException("Cannot set balance on a frozen account");
        }
    }

    public static CurrentCheckingAccount fromDTO(CurrentCheckingAccountDTO currentCheckingAccountDTO, AccountHolder primaryOwner, AccountHolder secondaryOwner) {

        // From Account model
        var account = Account.fromDTO(currentCheckingAccountDTO, primaryOwner, secondaryOwner);
        var currentCheckingAccount = new CurrentCheckingAccount();
        currentCheckingAccount.setIban(account.getIban());
        currentCheckingAccount.setBalance(account.getBalance());
        currentCheckingAccount.setPrimaryOwner(account.getPrimaryOwner());
        currentCheckingAccount.setSecondaryOwner(account.getSecondaryOwner());

        // From Current Account model
        currentCheckingAccount.setSecretKey(currentCheckingAccountDTO.getSecretKey());
        if (currentCheckingAccountDTO.getCreationDate() != null) {
            var creationDate = DateService.parseDate(currentCheckingAccountDTO.getCreationDate());
            currentCheckingAccount.setCreationDate(creationDate);
        }

        if (currentCheckingAccountDTO.getLastMonthlyFeeDate() != null) {
            var lastMonthlyFeeDate = DateService.parseDate(currentCheckingAccountDTO.getLastMonthlyFeeDate());
            currentCheckingAccount.setLastMonthlyFeeDate(lastMonthlyFeeDate);
        }

        if (currentCheckingAccountDTO.getStatus() != null) {
            currentCheckingAccount.setStatus(currentCheckingAccountDTO.getStatus());
        }

        return currentCheckingAccount;
    }
}
