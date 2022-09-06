package com.ironhack.ironbank.service;

import com.ironhack.ironbank.dto.AccountDTO;

import javax.validation.Valid;
import java.util.List;

public interface AccountService {

    AccountDTO create(@Valid AccountDTO accountDTO);
    AccountDTO findByIban(@Valid String iban);
    List<AccountDTO> findAll();
    AccountDTO update(@Valid String iban, @Valid AccountDTO accountDTO);
    void delete(@Valid String iban);
}
