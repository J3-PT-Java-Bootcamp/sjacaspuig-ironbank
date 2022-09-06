package com.ironhack.ironbank.service;

import com.ironhack.ironbank.dto.CurrentStudentCheckingAccountDTO;
import com.ironhack.ironbank.repository.CurrentStudentCheckingAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CurrentStudentCheckingAccountServiceImpl implements CurrentStudentCheckingAccountService {

    private final CurrentStudentCheckingAccountRepository currentStudentCheckingAccountRepository;

    @Override
    public CurrentStudentCheckingAccountDTO create(CurrentStudentCheckingAccountDTO currentStudentCheckingAccountDTO) {
        return null;
    }

    @Override
    public CurrentStudentCheckingAccountDTO findByIban(String iban) {
        return null;
    }

    @Override
    public List<CurrentStudentCheckingAccountDTO> findAll() {
        return null;
    }

    @Override
    public CurrentStudentCheckingAccountDTO update(String iban, CurrentStudentCheckingAccountDTO currentStudentCheckingAccountDTO) {
        return null;
    }

    @Override
    public void delete(String iban) {

    }
}
