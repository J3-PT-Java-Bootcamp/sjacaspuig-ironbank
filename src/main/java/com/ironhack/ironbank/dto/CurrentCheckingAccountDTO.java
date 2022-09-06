package com.ironhack.ironbank.dto;

import com.ironhack.ironbank.model.account.CurrentCheckingAccount;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CurrentCheckingAccountDTO extends CurrentAccountDTO {

    public static CurrentCheckingAccountDTO fromEntity(CurrentCheckingAccount currentCheckingAccount) {

        // From Account DTO model
        var currentCheckingAccountDTO = new CurrentCheckingAccountDTO();
        currentCheckingAccountDTO.setIban(currentCheckingAccount.getIban());

        var balanceDTO = MoneyDTO.fromEntity(currentCheckingAccount.getBalance());
        currentCheckingAccountDTO.setBalance(balanceDTO);

        var primaryOwnerDTO = AccountHolderDTO.fromEntity(currentCheckingAccount.getPrimaryOwner());
        currentCheckingAccountDTO.setPrimaryOwner(primaryOwnerDTO);

        if (currentCheckingAccount.getSecondaryOwner() != null) {
            var secondaryOwnerDTO = AccountHolderDTO.fromEntity(currentCheckingAccount.getSecondaryOwner());
            currentCheckingAccountDTO.setSecondaryOwner(secondaryOwnerDTO);
        }

        // From Current Account DTO model
        currentCheckingAccountDTO.setSecretKey(currentCheckingAccount.getSecretKey());
        currentCheckingAccountDTO.setCreationDate(currentCheckingAccount.getCreationDate());
        currentCheckingAccountDTO.setStatus(currentCheckingAccount.getStatus());

        return currentCheckingAccountDTO;
    }
}
