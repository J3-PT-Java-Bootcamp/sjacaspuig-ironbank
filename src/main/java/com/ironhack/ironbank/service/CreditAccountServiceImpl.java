package com.ironhack.ironbank.service;

import com.ironhack.ironbank.dto.CreditAccountDTO;
import com.ironhack.ironbank.repository.CreditAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CreditAccountServiceImpl implements CreditAccountService {

    private final CreditAccountRepository creditAccountRepository;

    @Override
    public CreditAccountDTO create(CreditAccountDTO creditAccountDTO) {
        return null;
    }

    @Override
    public CreditAccountDTO findById(Long id) {
        return null;
    }

    @Override
    public List<CreditAccountDTO> findAll() {
        return null;
    }

    @Override
    public CreditAccountDTO update(Long id, CreditAccountDTO creditAccountDTO) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }
}
