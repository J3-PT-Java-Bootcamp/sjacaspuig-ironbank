package com.ironhack.ironbank.dto;

import com.ironhack.ironbank.model.account.Account;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AccountDTO {

    private String iban;
    private MoneyDTO balance;
    protected AccountHolderDTO primaryOwner;
    protected AccountHolderDTO secondaryOwner;

    public static AccountDTO fromEntity(Account account) {
        var accountDTO = new AccountDTO();
        accountDTO.setIban(account.getIban());

        var balanceDTO = MoneyDTO.fromEntity(account.getBalance());
        accountDTO.setBalance(balanceDTO);

        var primaryOwnerDTO = AccountHolderDTO.fromEntity(account.getPrimaryOwner());
        accountDTO.setPrimaryOwner(primaryOwnerDTO);

        if (account.getSecondaryOwner() != null) {
            var secondaryOwnerDTO = AccountHolderDTO.fromEntity(account.getSecondaryOwner());
            accountDTO.setSecondaryOwner(secondaryOwnerDTO);
        }

        return accountDTO;
    }
}