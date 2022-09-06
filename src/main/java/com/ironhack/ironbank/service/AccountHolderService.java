package com.ironhack.ironbank.service;

import com.ironhack.ironbank.dto.AccountHolderDTO;

import java.util.List;

public interface AccountHolderService {

    AccountHolderDTO create(AccountHolderDTO accountHolderDTO);
    AccountHolderDTO findById(Long id);
    List<AccountHolderDTO> findAll();
    AccountHolderDTO update(Long id, AccountHolderDTO accountHolderDTO);
    void delete(Long id);
}
