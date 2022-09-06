package com.ironhack.ironbank.service;

import com.ironhack.ironbank.dto.CurrentCheckingAccountDTO;
import com.ironhack.ironbank.repository.CurrentCheckingAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CurrentCheckingAccountServiceImpl implements CurrentCheckingAccountService {

    private final CurrentCheckingAccountRepository currentCheckingAccountRepository;

    @Override
    public CurrentCheckingAccountDTO create(CurrentCheckingAccountDTO currentCheckingAccountDTO) {
        return null;
    }

    @Override
    public CurrentCheckingAccountDTO findByIban(String iban) {
        return null;
    }

    @Override
    public List<CurrentCheckingAccountDTO> findAll() {
        return null;
    }

    @Override
    public CurrentCheckingAccountDTO update(String iban, CurrentCheckingAccountDTO currentCheckingAccountDTO) {
        return null;
    }

    @Override
    public void delete(String iban) {

    }
}
