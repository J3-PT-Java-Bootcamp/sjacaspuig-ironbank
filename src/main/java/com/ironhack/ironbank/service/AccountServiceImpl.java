package com.ironhack.ironbank.service;

import com.ironhack.ironbank.dto.AccountDTO;
import com.ironhack.ironbank.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    @Override
    public AccountDTO create(AccountDTO accountDTO) {
        return null;
    }

    @Override
    public AccountDTO findByIban(String iban) {
        return null;
    }

    @Override
    public List<AccountDTO> findAll() {
        return null;
    }

    @Override
    public AccountDTO update(String iban, AccountDTO accountDTO) {
        return null;
    }

    @Override
    public void delete(String iban) {

    }
}
