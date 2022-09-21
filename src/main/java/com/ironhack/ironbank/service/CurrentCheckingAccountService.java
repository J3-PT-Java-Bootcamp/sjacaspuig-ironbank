package com.ironhack.ironbank.service;

import com.ironhack.ironbank.dto.AccountStatusDTO;
import com.ironhack.ironbank.dto.CurrentCheckingAccountDTO;
import com.ironhack.ironbank.model.account.CurrentCheckingAccount;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

public interface CurrentCheckingAccountService {

    CurrentCheckingAccountDTO create(@Valid CurrentCheckingAccountDTO currentCheckingAccountDTO);
    CurrentCheckingAccountDTO findByIban(@Valid String iban);
    Optional<CurrentCheckingAccount> findEntity(@Valid String iban);
    List<CurrentCheckingAccountDTO> findAll();
    List<CurrentCheckingAccountDTO> findByAccountHolderId(@Valid String id);
    List<CurrentCheckingAccount> findAllEntities();
    CurrentCheckingAccountDTO update(@Valid String iban, @Valid CurrentCheckingAccountDTO currentCheckingAccountDTO);
    void delete(@Valid String iban);
    CurrentCheckingAccountDTO changeStatus(@Valid String iban, @Valid AccountStatusDTO accountStatusDTO);
}
