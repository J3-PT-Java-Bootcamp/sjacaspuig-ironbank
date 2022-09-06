package com.ironhack.ironbank.dto;

import com.ironhack.ironbank.model.account.CurrentSavingsAccount;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CurrentSavingsAccountDTO extends CurrentAccountDTO {

    private MoneyDTO minimumBalance;
    private BigDecimal interestRate;
    private Instant interestRateDate;

    public static CurrentSavingsAccountDTO fromEntity(CurrentSavingsAccount currentSavingsAccount) {

        // From Account DTO model
        var account = AccountDTO.fromEntity(currentSavingsAccount);
        var currentSavingsAccountDTO = new CurrentSavingsAccountDTO();
        currentSavingsAccountDTO.setIban(account.getIban());
        currentSavingsAccountDTO.setBalance(account.getBalance());
        currentSavingsAccountDTO.setPrimaryOwner(account.getPrimaryOwner());
        currentSavingsAccountDTO.setSecondaryOwner(account.getSecondaryOwner());

        // From Current Account DTO model
        currentSavingsAccountDTO.setSecretKey(currentSavingsAccount.getSecretKey());
        currentSavingsAccountDTO.setCreationDate(currentSavingsAccount.getCreationDate());
        currentSavingsAccountDTO.setStatus(currentSavingsAccount.getStatus());

        // From Current Savings Account DTO model
        var minimumBalanceDTO = MoneyDTO.fromEntity(currentSavingsAccount.getMinimumBalance());
        currentSavingsAccountDTO.setMinimumBalance(minimumBalanceDTO);

        currentSavingsAccountDTO.setInterestRate(currentSavingsAccount.getInterestRate());
        currentSavingsAccountDTO.setInterestRateDate(currentSavingsAccount.getInterestRateDate());

        return currentSavingsAccountDTO;
    }
}
