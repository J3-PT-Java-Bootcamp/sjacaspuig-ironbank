package com.ironhack.ironbank.service;

import com.ironhack.ironbank.dto.CurrentSavingsAccountDTO;
import com.ironhack.ironbank.repository.CurrentSavingsAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CurrentSavingsAccountServiceImpl implements CurrentSavingsAccountService {

    private final CurrentSavingsAccountRepository currentSavingsAccountRepository;

    @Override
    public CurrentSavingsAccountDTO create(CurrentSavingsAccountDTO currentSavingsAccountDTO) {
        return null;
    }

    @Override
    public CurrentSavingsAccountDTO findByIban(String iban) {
        return null;
    }

    @Override
    public List<CurrentSavingsAccountDTO> findAll() {
        return null;
    }

    @Override
    public CurrentSavingsAccountDTO update(String iban, CurrentSavingsAccountDTO currentSavingsAccountDTO) {
        return null;
    }

    @Override
    public void delete(String iban) {

    }
}
