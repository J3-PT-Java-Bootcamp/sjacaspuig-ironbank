package com.ironhack.ironbank.service;

import com.ironhack.ironbank.dto.AccountHolderDTO;

import javax.validation.Valid;
import java.util.List;

public interface AccountHolderService {

    AccountHolderDTO create(@Valid AccountHolderDTO accountHolderDTO);
    AccountHolderDTO findById(@Valid Long id);
    List<AccountHolderDTO> findAll();
    AccountHolderDTO update(@Valid Long id, @Valid AccountHolderDTO accountHolderDTO);
    void delete(@Valid Long id);
}
