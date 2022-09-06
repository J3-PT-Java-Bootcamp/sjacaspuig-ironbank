package com.ironhack.ironbank.dto;

import com.ironhack.ironbank.model.user.AccountHolder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AccountHolderDTO extends UserDTO {

    private Instant birthDate;
    private AddressDTO primaryAddress;
    private AddressDTO secondaryAddress;

    public static AccountHolderDTO fromEntity(AccountHolder accountHolder) {
        var accountHolderDTO = new AccountHolderDTO();
        accountHolderDTO.setId(accountHolder.getId());
        accountHolderDTO.setName(accountHolder.getName());
        accountHolderDTO.setBirthDate(accountHolder.getBirthDate());

        var primaryAddressDTO = AddressDTO.fromEntity(accountHolder.getPrimaryAddress());
        accountHolderDTO.setPrimaryAddress(primaryAddressDTO);

        var secondaryAddressDTO = AddressDTO.fromEntity(accountHolder.getSecondaryAddress());
        accountHolderDTO.setSecondaryAddress(secondaryAddressDTO);

        return accountHolderDTO;
    }
}
