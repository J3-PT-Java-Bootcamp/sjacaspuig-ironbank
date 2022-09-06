package com.ironhack.ironbank.service;

import com.ironhack.ironbank.dto.CurrentStudentCheckingAccountDTO;

import javax.validation.Valid;
import java.util.List;
public interface CurrentStudentCheckingAccountService {

    CurrentStudentCheckingAccountDTO create(@Valid CurrentStudentCheckingAccountDTO currentStudentCheckingAccountDTO);
    CurrentStudentCheckingAccountDTO findByIban(@Valid String iban);
    List<CurrentStudentCheckingAccountDTO> findAll();
    CurrentStudentCheckingAccountDTO update(@Valid String iban, @Valid CurrentStudentCheckingAccountDTO currentStudentCheckingAccountDTO);
    void delete(@Valid String iban);
}
