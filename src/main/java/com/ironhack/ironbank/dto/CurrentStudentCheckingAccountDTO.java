package com.ironhack.ironbank.dto;

import com.ironhack.ironbank.model.account.CurrentStudentCheckingAccount;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CurrentStudentCheckingAccountDTO extends CurrentAccountDTO {

    public static CurrentStudentCheckingAccountDTO fromEntity(CurrentStudentCheckingAccount currentStudentCheckingAccount) {

        // From Account DTO model
        var account = AccountDTO.fromEntity(currentStudentCheckingAccount);
        var currentStudentCheckingAccountDTO = new CurrentStudentCheckingAccountDTO();
        currentStudentCheckingAccountDTO.setIban(account.getIban());
        currentStudentCheckingAccountDTO.setBalance(account.getBalance());
        currentStudentCheckingAccountDTO.setPrimaryOwner(account.getPrimaryOwner());
        currentStudentCheckingAccountDTO.setSecondaryOwner(account.getSecondaryOwner());

        // From Current Account DTO model
        currentStudentCheckingAccountDTO.setSecretKey(currentStudentCheckingAccount.getSecretKey());
        currentStudentCheckingAccountDTO.setCreationDate(currentStudentCheckingAccount.getCreationDate());
        currentStudentCheckingAccountDTO.setStatus(currentStudentCheckingAccount.getStatus());

        return currentStudentCheckingAccountDTO;
    }
}
