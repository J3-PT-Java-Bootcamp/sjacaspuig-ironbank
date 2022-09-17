package com.ironhack.ironbank.service;

import com.ironhack.ironbank.dto.AccountDTO;
import com.ironhack.ironbank.model.account.Account;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

public interface AccountService {

    AccountDTO findByIban(@Valid String iban);
    Optional<? extends Account> findById(@Valid String iban);
    List<AccountDTO> findAll();
    List<Account> findAllAccounts();
    void delete(@Valid String iban);
}
