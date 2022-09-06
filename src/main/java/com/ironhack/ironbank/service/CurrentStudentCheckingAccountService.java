package com.ironhack.ironbank.service;

import com.ironhack.ironbank.dto.CurrentStudentCheckingAccountDTO;

import java.util.List;
public interface CurrentStudentCheckingAccountService {

    CurrentStudentCheckingAccountDTO create(CurrentStudentCheckingAccountDTO currentStudentCheckingAccountDTO);
    CurrentStudentCheckingAccountDTO findById(Long id);
    List<CurrentStudentCheckingAccountDTO> findAll();
    CurrentStudentCheckingAccountDTO update(Long id, CurrentStudentCheckingAccountDTO currentStudentCheckingAccountDTO);
    void delete(Long id);
}
