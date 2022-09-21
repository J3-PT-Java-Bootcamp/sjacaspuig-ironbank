package com.ironhack.ironbank.service;

import com.ironhack.ironbank.dto.CreditAccountDTO;
import com.ironhack.ironbank.model.account.CreditAccount;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

public interface CreditAccountService {

    CreditAccountDTO create(@Valid CreditAccountDTO creditAccountDTO);
    CreditAccountDTO findByIban(@Valid String iban);
    Optional<CreditAccount> findEntity(@Valid String iban);
    List<CreditAccountDTO> findAll();
    List<CreditAccountDTO> findByAccountHolderId(@Valid String id);
    List<CreditAccount> findAllEntities();
    CreditAccountDTO update(@Valid String iban, @Valid CreditAccountDTO creditAccountDTO);
    void delete(@Valid String iban);

}
