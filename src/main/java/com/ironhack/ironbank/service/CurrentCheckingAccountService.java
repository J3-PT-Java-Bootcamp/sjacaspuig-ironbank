package com.ironhack.ironbank.service;

import com.ironhack.ironbank.dto.CurrentCheckingAccountDTO;

import javax.validation.Valid;
import java.util.List;
public interface CurrentCheckingAccountService {

    CurrentCheckingAccountDTO create(@Valid CurrentCheckingAccountDTO currentCheckingAccountDTO);
    CurrentCheckingAccountDTO findByIban(@Valid String iban);
    List<CurrentCheckingAccountDTO> findAll();
    CurrentCheckingAccountDTO update(@Valid String iban, @Valid CurrentCheckingAccountDTO currentCheckingAccountDTO);
    void delete(@Valid String iban);
}
