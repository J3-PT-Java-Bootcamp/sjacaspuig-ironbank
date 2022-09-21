package com.ironhack.ironbank.service;

import com.ironhack.ironbank.dto.AccountDTO;
import com.ironhack.ironbank.dto.AccountStatusDTO;
import com.ironhack.ironbank.model.account.Account;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

public interface AccountService {

    AccountDTO findByIban(@Valid String iban);
    Optional<? extends Account> findById(@Valid String iban);
    List<AccountDTO> findAll();
    List<AccountDTO> findByAccountHolderId(@Valid String id);
    List<Account> findAllAccounts();
    void delete(@Valid String iban);
    AccountDTO changeStatus(@Valid String iban, @Valid AccountStatusDTO accountStatusDTO);
}
