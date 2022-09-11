package com.ironhack.ironbank.service;

import com.ironhack.ironbank.dto.AccountHolderDTO;
import com.ironhack.ironbank.model.user.AccountHolder;

import javax.validation.Valid;
import java.util.List;


public interface AccountHolderService {

    AccountHolderDTO create(@Valid AccountHolderDTO accountHolderDTO, String password);
    AccountHolderDTO findById(@Valid String id);
    List<AccountHolderDTO> findAll();
    AccountHolderDTO update(@Valid String id, @Valid AccountHolderDTO accountHolderDTO);
    void delete(@Valid String id);
    AccountHolder findOwnerById(@Valid String id);
}
