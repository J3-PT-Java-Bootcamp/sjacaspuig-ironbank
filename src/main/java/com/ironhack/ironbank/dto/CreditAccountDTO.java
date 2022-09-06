package com.ironhack.ironbank.dto;

import com.ironhack.ironbank.model.account.Account;
import com.ironhack.ironbank.model.account.CreditAccount;
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
public class CreditAccountDTO extends AccountDTO {

    private MoneyDTO creditLimit;
    private BigDecimal interestRate;
    private Instant interestRateDate;

    public static CreditAccountDTO fromEntity(CreditAccount creditAccount) {

        // From Account DTO model
        var account = AccountDTO.fromEntity(creditAccount);
        var creditAccountDTO = new CreditAccountDTO();
        creditAccountDTO.setIban(account.getIban());
        creditAccountDTO.setBalance(account.getBalance());
        creditAccountDTO.setPrimaryOwner(account.getPrimaryOwner());
        creditAccountDTO.setSecondaryOwner(account.getSecondaryOwner());

        // From Credit Account DTO model
        var moneyDTO = MoneyDTO.fromEntity(creditAccount.getCreditLimit());
        creditAccountDTO.setCreditLimit(moneyDTO);
        creditAccountDTO.setInterestRate(creditAccount.getInterestRate());
        creditAccountDTO.setInterestRateDate(creditAccount.getInterestRateDate());

        return creditAccountDTO;
    }
}
