package com.ironhack.ironbank.service;

import com.ironhack.ironbank.dto.AccountDTO;

import java.util.List;

public interface AccountService {

    AccountDTO create(AccountDTO accountDTO);
    AccountDTO findById(Long id);
    List<AccountDTO> findAll();
    AccountDTO update(Long id, AccountDTO accountDTO);
    void delete(Long id);
}
