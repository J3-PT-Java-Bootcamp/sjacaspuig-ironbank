package com.ironhack.ironbank.model.account;

import com.ironhack.ironbank.constants.AccountConstants;
import com.ironhack.ironbank.dto.CurrentStudentCheckingAccountDTO;
import com.ironhack.ironbank.enums.AccountStatus;
import com.ironhack.ironbank.enums.AccountType;
import com.ironhack.ironbank.model.Money;
import com.ironhack.ironbank.model.user.AccountHolder;
import com.ironhack.ironbank.utils.DateService;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
@Table(name = "current_student_checking_accounts")
public class CurrentStudentCheckingAccount extends CurrentAccount {

    private static final int MAX_AGE = AccountConstants.CHECKING_ACCOUNT_MIN_AGE;

    public CurrentStudentCheckingAccount() {
        setAccountType(AccountType.STUDENT_CHECKING);
        super.setStatus(AccountStatus.ACTIVE);
    }

    @Override
    public void setPrimaryOwner(@NotNull AccountHolder primaryOwner) {
        super.setPrimaryOwner(primaryOwner);
    }

    @Override
    public AccountHolder getPrimaryOwner() {
        return super.getPrimaryOwner();
    }

    @Override
    public void setBalance(@NotNull Money balance) {
        if (getStatus() == AccountStatus.ACTIVE) {
            super.setBalance(balance);
        } else {
            throw new IllegalArgumentException("The status of the account with id " + getIban() + " is " + getStatus() + " and its balance cannot be modified");
        }
    }

    public static CurrentStudentCheckingAccount fromDTO(CurrentStudentCheckingAccountDTO currentStudentCheckingAccountDTO, AccountHolder primaryOwner, AccountHolder secondaryOwner) {

        // From Account model
        var account = Account.fromDTO(currentStudentCheckingAccountDTO, primaryOwner, secondaryOwner);
        var currentStudentCheckingAccount = new CurrentStudentCheckingAccount();
        currentStudentCheckingAccount.setIban(account.getIban());
        currentStudentCheckingAccount.setBalance(account.getBalance());
        currentStudentCheckingAccount.setPrimaryOwner(account.getPrimaryOwner());
        currentStudentCheckingAccount.setSecondaryOwner(account.getSecondaryOwner());

        // From Current Account model
        currentStudentCheckingAccount.setSecretKey(currentStudentCheckingAccountDTO.getSecretKey());
        if (currentStudentCheckingAccountDTO.getCreationDate() != null) {
            var creationDate = DateService.parseDate(currentStudentCheckingAccountDTO.getCreationDate());
            currentStudentCheckingAccount.setCreationDate(creationDate);
        }

        if (currentStudentCheckingAccountDTO.getStatus() != null) {
            currentStudentCheckingAccount.setStatus(currentStudentCheckingAccountDTO.getStatus());
        }

        return currentStudentCheckingAccount;
    }

    private boolean isOwnerAllowedToCreate(AccountHolder accountHolder) {
        return DateService.getDiffYears(accountHolder.getBirthDate()) < MAX_AGE;
    }
}
