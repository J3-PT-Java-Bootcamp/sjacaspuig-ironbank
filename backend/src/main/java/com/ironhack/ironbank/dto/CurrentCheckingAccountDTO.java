package com.ironhack.ironbank.dto;

import com.ironhack.ironbank.model.account.CurrentCheckingAccount;
import com.ironhack.ironbank.utils.DateService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CurrentCheckingAccountDTO extends CurrentAccountDTO {

    private Date lastMonthlyFeeDate;

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
        currentCheckingAccountDTO.setAccountType(currentCheckingAccount.getAccountType());

        if (currentCheckingAccount.getLastMonthlyFeeDate() != null) {
            var lastMonthlyFeeDate = DateService.parseInstant(currentCheckingAccount.getLastMonthlyFeeDate());
            currentCheckingAccountDTO.setLastMonthlyFeeDate(lastMonthlyFeeDate);
        }

        return currentCheckingAccountDTO;
    }

    public static List<CurrentCheckingAccountDTO> fromList(List<CurrentCheckingAccount> currentCheckingAccounts) {
        return currentCheckingAccounts.stream().map(CurrentCheckingAccountDTO::fromEntity).toList();
    }
}
