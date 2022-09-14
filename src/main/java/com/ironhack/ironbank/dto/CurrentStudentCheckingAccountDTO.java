package com.ironhack.ironbank.dto;

import com.ironhack.ironbank.model.account.CurrentStudentCheckingAccount;
import com.ironhack.ironbank.utils.DateService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class CurrentStudentCheckingAccountDTO extends CurrentAccountDTO {

    public static CurrentStudentCheckingAccountDTO fromEntity(CurrentStudentCheckingAccount currentStudentCheckingAccount) {

        // From Account DTO model
        var account = AccountDTO.fromEntity(currentStudentCheckingAccount);
        var currentStudentCheckingAccountDTO = new CurrentStudentCheckingAccountDTO();
        currentStudentCheckingAccountDTO.setIban(account.getIban());
        currentStudentCheckingAccountDTO.setBalance(account.getBalance());
        currentStudentCheckingAccountDTO.setPrimaryOwner(account.getPrimaryOwner());
        currentStudentCheckingAccountDTO.setSecondaryOwner(account.getSecondaryOwner());

        // From Current Account DTO model
        currentStudentCheckingAccountDTO.setSecretKey(currentStudentCheckingAccount.getSecretKey());
        var creationDateDTO = DateService.parseInstant(currentStudentCheckingAccount.getCreationDate());
        currentStudentCheckingAccountDTO.setCreationDate(creationDateDTO);
        currentStudentCheckingAccountDTO.setStatus(currentStudentCheckingAccount.getStatus());

        return currentStudentCheckingAccountDTO;
    }

    public static List<CurrentStudentCheckingAccountDTO> fromList(List<CurrentStudentCheckingAccount> currentStudentCheckingAccounts) {
        return currentStudentCheckingAccounts.stream().map(CurrentStudentCheckingAccountDTO::fromEntity).toList();
    }

    public static CurrentStudentCheckingAccountDTO fromCurrentAccountDTO(CurrentAccountDTO currentAccountDTO) {
        var studentAccountDTO = new CurrentStudentCheckingAccountDTO();
        studentAccountDTO.setIban(currentAccountDTO.getIban());
        studentAccountDTO.setBalance(currentAccountDTO.getBalance());
        studentAccountDTO.setPrimaryOwner(currentAccountDTO.getPrimaryOwner());
        studentAccountDTO.setSecondaryOwner(currentAccountDTO.getSecondaryOwner());
        studentAccountDTO.setSecretKey(currentAccountDTO.getSecretKey());
        studentAccountDTO.setCreationDate(currentAccountDTO.getCreationDate());
        studentAccountDTO.setStatus(currentAccountDTO.getStatus());
        return studentAccountDTO;
    }
}
