package com.ironhack.ironbank.dto;

import com.ironhack.ironbank.model.account.CreditAccount;
import com.ironhack.ironbank.utils.DateService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreditAccountDTO extends AccountDTO {

    private MoneyDTO creditLimit;
    private BigDecimal interestRate;
    private Date interestRateDate;

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

        if(creditAccount.getInterestRateDate() != null) {
            var interestRateDateDTO = DateService.parseInstant(creditAccount.getInterestRateDate());
            creditAccountDTO.setInterestRateDate(interestRateDateDTO);
        }

        creditAccountDTO.setAccountType(creditAccount.getAccountType());

        return creditAccountDTO;
    }

    public static List<CreditAccountDTO> fromList(List<CreditAccount> creditAccounts) {
        return creditAccounts.stream().map(CreditAccountDTO::fromEntity).toList();
    }
}
