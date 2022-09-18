package com.ironhack.ironbank.service;

import com.ironhack.ironbank.dto.CurrentSavingsAccountDTO;
import com.ironhack.ironbank.model.account.CurrentSavingsAccount;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

public interface CurrentSavingsAccountService {

    CurrentSavingsAccountDTO create(@Valid CurrentSavingsAccountDTO currentSavingsAccountDTO);
    CurrentSavingsAccountDTO findByIban(@Valid String iban);
    Optional<CurrentSavingsAccount> findEntity(@Valid String iban);
    List<CurrentSavingsAccountDTO> findAll();
    List<CurrentSavingsAccount> findAllEntities();
    CurrentSavingsAccountDTO update(@Valid String iban, @Valid CurrentSavingsAccountDTO currentSavingsAccountDTO);
    void delete(@Valid String iban);

}
