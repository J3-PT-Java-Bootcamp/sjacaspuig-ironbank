package com.ironhack.ironbank.service;

import com.ironhack.ironbank.dto.CreditAccountDTO;

import javax.validation.Valid;
import java.util.List;
public interface CreditAccountService {

    CreditAccountDTO create(@Valid CreditAccountDTO creditAccountDTO);
    CreditAccountDTO findByIban(@Valid String iban);
    List<CreditAccountDTO> findAll();
    CreditAccountDTO update(@Valid String iban, @Valid CreditAccountDTO creditAccountDTO);
    void delete(@Valid String iban);

}
