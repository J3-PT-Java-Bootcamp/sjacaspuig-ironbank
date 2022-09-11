package com.ironhack.ironbank.dto;

import com.ironhack.ironbank.model.user.AccountHolder;
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
public class AccountHolderDTO extends UserSecurityDTO {

    private Date birthDate;
    private AddressDTO primaryAddress;
    private AddressDTO secondaryAddress;

    public static AccountHolderDTO fromEntity(AccountHolder accountHolder) {
        var accountHolderDTO = new AccountHolderDTO();
        accountHolderDTO.setId(accountHolder.getId());
        accountHolderDTO.setFirstName(accountHolder.getFirstName());
        accountHolderDTO.setLastName(accountHolder.getLastName());
        accountHolderDTO.setUsername(accountHolder.getUsername());
        accountHolderDTO.setEmail(accountHolder.getEmail());
        var birthDate = DateService.parseInstant(accountHolder.getBirthDate());
        accountHolderDTO.setBirthDate(birthDate);

        var primaryAddressDTO = AddressDTO.fromEntity(accountHolder.getPrimaryAddress());
        accountHolderDTO.setPrimaryAddress(primaryAddressDTO);

        if (accountHolder.getSecondaryAddress() != null) {
            var secondaryAddressDTO = AddressDTO.fromEntity(accountHolder.getSecondaryAddress());
            accountHolderDTO.setSecondaryAddress(secondaryAddressDTO);
        }

        return accountHolderDTO;
    }

    public static List<AccountHolderDTO> fromEntities(List<AccountHolder> accountHolders) {
        return accountHolders.stream().map(AccountHolderDTO::fromEntity).toList();
    }
}
