package com.ironhack.ironbank.service;

import com.ironhack.ironbank.dto.CreditAccountDTO;

import java.util.List;
public interface CreditAccountService {

    CreditAccountDTO create(CreditAccountDTO creditAccountDTO);
    CreditAccountDTO findById(Long id);
    List<CreditAccountDTO> findAll();
    CreditAccountDTO update(Long id, CreditAccountDTO creditAccountDTO);
    void delete(Long id);

}
