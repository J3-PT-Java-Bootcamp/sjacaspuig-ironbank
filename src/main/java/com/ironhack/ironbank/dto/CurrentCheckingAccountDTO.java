package com.ironhack.ironbank.dto;

import com.ironhack.ironbank.model.account.CurrentCheckingAccount;
import com.ironhack.ironbank.utils.DateService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class CurrentCheckingAccountDTO extends CurrentAccountDTO {

    public static CurrentCheckingAccountDTO fromEntity(CurrentCheckingAccount currentCheckingAccount) {

        // From Account DTO model
        var account = AccountDTO.fromEntity(currentCheckingAccount);
        var currentCheckingAccountDTO = new CurrentCheckingAccountDTO();
        currentCheckingAccountDTO.setIban(account.getIban());
        currentCheckingAccountDTO.setBalance(account.getBalance());
        currentCheckingAccountDTO.setPrimaryOwner(account.getPrimaryOwner());
        currentCheckingAccountDTO.setSecondaryOwner(account.getSecondaryOwner());

        // From Current Account DTO model
        currentCheckingAccountDTO.setSecretKey(currentCheckingAccount.getSecretKey());
        var creationDateDTO = DateService.parseInstant(currentCheckingAccount.getCreationDate());
        currentCheckingAccountDTO.setCreationDate(creationDateDTO);
        currentCheckingAccountDTO.setStatus(currentCheckingAccount.getStatus());

        return currentCheckingAccountDTO;
    }
}
