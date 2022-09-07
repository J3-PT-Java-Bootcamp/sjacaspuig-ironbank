package com.ironhack.ironbank.dto;

import com.ironhack.ironbank.model.account.CurrentSavingsAccount;
import com.ironhack.ironbank.utils.DateService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CurrentSavingsAccountDTO extends CurrentAccountDTO {

    private MoneyDTO minimumBalance;
    private BigDecimal interestRate;
    private Date interestRateDate;

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
        if (currentSavingsAccount.getCreationDate() != null) {
            var creationDateDTO = DateService.parseInstant(currentSavingsAccount.getCreationDate());
            currentSavingsAccountDTO.setCreationDate(creationDateDTO);
        }
        currentSavingsAccountDTO.setStatus(currentSavingsAccount.getStatus());

        // From Current Savings Account DTO model
        var minimumBalanceDTO = MoneyDTO.fromEntity(currentSavingsAccount.getMinimumBalance());
        currentSavingsAccountDTO.setMinimumBalance(minimumBalanceDTO);

        currentSavingsAccountDTO.setInterestRate(currentSavingsAccount.getInterestRate());

        if (currentSavingsAccount.getInterestRateDate() != null) {
            var interestRateDateDTO = DateService.parseInstant(currentSavingsAccount.getInterestRateDate());
            currentSavingsAccountDTO.setInterestRateDate(interestRateDateDTO);
        }

        return currentSavingsAccountDTO;
    }
}
