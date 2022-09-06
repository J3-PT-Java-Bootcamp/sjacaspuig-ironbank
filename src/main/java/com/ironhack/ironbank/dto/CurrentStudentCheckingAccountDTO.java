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
        var currentStudentCheckingAccountDTO = new CurrentStudentCheckingAccountDTO();
        currentStudentCheckingAccountDTO.setIban(currentStudentCheckingAccount.getIban());

        var balanceDTO = MoneyDTO.fromEntity(currentStudentCheckingAccount.getBalance());
        currentStudentCheckingAccountDTO.setBalance(balanceDTO);

        var primaryOwnerDTO = AccountHolderDTO.fromEntity(currentStudentCheckingAccount.getPrimaryOwner());
        currentStudentCheckingAccountDTO.setPrimaryOwner(primaryOwnerDTO);

        if (currentStudentCheckingAccount.getSecondaryOwner() != null) {
            var secondaryOwnerDTO = AccountHolderDTO.fromEntity(currentStudentCheckingAccount.getSecondaryOwner());
            currentStudentCheckingAccountDTO.setSecondaryOwner(secondaryOwnerDTO);
        }

        // From Current Account DTO model
        currentStudentCheckingAccountDTO.setSecretKey(currentStudentCheckingAccount.getSecretKey());
        currentStudentCheckingAccountDTO.setCreationDate(currentStudentCheckingAccount.getCreationDate());
        currentStudentCheckingAccountDTO.setStatus(currentStudentCheckingAccount.getStatus());

        return currentStudentCheckingAccountDTO;
    }
}
