package com.ironhack.ironbank.model.account;

import com.ironhack.ironbank.dto.CurrentStudentCheckingAccountDTO;
import com.ironhack.ironbank.model.user.AccountHolder;
import com.ironhack.ironbank.utils.DateService;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "current_student_checking_accounts")
public class CurrentStudentCheckingAccount extends CurrentAccount {

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
        currentStudentCheckingAccount.setStatus(currentStudentCheckingAccountDTO.getStatus());

        return currentStudentCheckingAccount;
    }
}
