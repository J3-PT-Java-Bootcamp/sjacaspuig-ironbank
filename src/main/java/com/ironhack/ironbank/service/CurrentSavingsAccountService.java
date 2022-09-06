package com.ironhack.ironbank.service;

import com.ironhack.ironbank.dto.CurrentSavingsAccountDTO;

import java.util.List;
public interface CurrentSavingsAccountService {

    CurrentSavingsAccountDTO create(CurrentSavingsAccountDTO currentSavingsAccountDTO);
    CurrentSavingsAccountDTO findById(Long id);
    List<CurrentSavingsAccountDTO> findAll();
    CurrentSavingsAccountDTO update(Long id, CurrentSavingsAccountDTO currentSavingsAccountDTO);
    void delete(Long id);
}
