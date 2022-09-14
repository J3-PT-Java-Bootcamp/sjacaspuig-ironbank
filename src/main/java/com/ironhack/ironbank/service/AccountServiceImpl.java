package com.ironhack.ironbank.service;

import com.ironhack.ironbank.dto.AccountDTO;
import com.ironhack.ironbank.model.account.Account;
import com.ironhack.ironbank.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
    public Optional<Account> findById(String iban) {
        return accountRepository.findById(iban);
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

    @Override
    public Account findByIbanAndSecretKey(String iban, String secretKey) {

        var account = accountRepository.findById(iban).orElseThrow(() -> new IllegalArgumentException("Account not found"));
        if (!account.getSecretKey().equals(secretKey)) {
            throw new IllegalArgumentException("Secret key is not valid");
        }
        return account;
    }
}
