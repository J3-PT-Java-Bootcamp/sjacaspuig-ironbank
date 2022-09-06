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
    public CurrentCheckingAccountDTO findById(Long id) {
        return null;
    }

    @Override
    public List<CurrentCheckingAccountDTO> findAll() {
        return null;
    }

    @Override
    public CurrentCheckingAccountDTO update(Long id, CurrentCheckingAccountDTO currentCheckingAccountDTO) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }
}
