package com.ironhack.ironbank.service;

import com.ironhack.ironbank.dto.CurrentSavingsAccountDTO;

import javax.validation.Valid;
import java.util.List;
public interface CurrentSavingsAccountService {

    CurrentSavingsAccountDTO create(@Valid CurrentSavingsAccountDTO currentSavingsAccountDTO);
    CurrentSavingsAccountDTO findByIban(@Valid String iban);
    List<CurrentSavingsAccountDTO> findAll();
    CurrentSavingsAccountDTO update(@Valid String iban, @Valid CurrentSavingsAccountDTO currentSavingsAccountDTO);
    void delete(@Valid String iban);
}
