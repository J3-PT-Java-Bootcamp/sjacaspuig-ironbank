package com.ironhack.ironbank.service;

import com.ironhack.ironbank.dto.AccountDTO;
import com.ironhack.ironbank.model.account.Account;
import com.ironhack.ironbank.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    @Override
    public AccountDTO findByIban(String iban) {
        var account = accountRepository.findById(iban).orElseThrow(() -> new IllegalArgumentException("Account not found"));
        return AccountDTO.fromEntity(account);
    }

    @Override
    public List<AccountDTO> findAll() {
        var accounts = accountRepository.findAll();
        return AccountDTO.fromEntities(accounts);
    }

    @Override
    public AccountDTO update(String iban, AccountDTO accountDTO) {
        var account = accountRepository.findById(iban).orElseThrow(() -> new IllegalArgumentException("Account not found"));
        var accountUpdated = Account.fromDTO(accountDTO, account.getPrimaryOwner(), account.getSecondaryOwner());
        accountUpdated.setIban(account.getIban());
        accountUpdated = accountRepository.save(accountUpdated);
        return AccountDTO.fromEntity(accountUpdated);
    }

    @Override
    public void delete(String iban) {
        accountRepository.deleteById(iban);
    }
}
