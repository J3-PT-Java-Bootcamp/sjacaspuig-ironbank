package com.ironhack.ironbank.service;

import com.ironhack.ironbank.dto.CurrentCheckingAccountDTO;

import java.util.List;
public interface CurrentCheckingAccountService {

    CurrentCheckingAccountDTO create(CurrentCheckingAccountDTO currentCheckingAccountDTO);
    CurrentCheckingAccountDTO findById(Long id);
    List<CurrentCheckingAccountDTO> findAll();
    CurrentCheckingAccountDTO update(Long id, CurrentCheckingAccountDTO currentCheckingAccountDTO);
    void delete(Long id);
}
