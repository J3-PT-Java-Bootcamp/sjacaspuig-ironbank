package com.ironhack.ironbank.service;

import com.ironhack.ironbank.dto.AccountStatusDTO;
import com.ironhack.ironbank.dto.CurrentStudentCheckingAccountDTO;
import com.ironhack.ironbank.model.account.CurrentStudentCheckingAccount;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

public interface CurrentStudentCheckingAccountService {

    CurrentStudentCheckingAccountDTO create(@Valid CurrentStudentCheckingAccountDTO currentStudentCheckingAccountDTO);
    CurrentStudentCheckingAccountDTO findByIban(@Valid String iban);
    Optional<CurrentStudentCheckingAccount> findEntity(@Valid String iban);
    List<CurrentStudentCheckingAccountDTO> findAll();
    List<CurrentStudentCheckingAccountDTO> findByAccountHolderId(@Valid String id);
    List<CurrentStudentCheckingAccount> findAllEntities();
    CurrentStudentCheckingAccountDTO update(@Valid String iban, @Valid CurrentStudentCheckingAccountDTO currentStudentCheckingAccountDTO);
    void delete(@Valid String iban);
    CurrentStudentCheckingAccountDTO changeStatus(@Valid String iban, @Valid AccountStatusDTO accountStatusDTO);
}
