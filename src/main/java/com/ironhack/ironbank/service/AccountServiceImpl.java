package com.ironhack.ironbank.service;

import com.ironhack.ironbank.dto.*;
import com.ironhack.ironbank.model.Money;
import com.ironhack.ironbank.model.account.*;
import com.ironhack.ironbank.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final CreditAccountRepository creditAccountRepository;
    private final CurrentCheckingAccountRepository currentCheckingAccountRepository;
    private final CurrentSavingsAccountRepository currentSavingsAccountRepository;
    private final CurrentStudentCheckingAccountRepository currentStudentCheckingAccountRepository;

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
        var accountUpdated = accountRepository.findById(iban).orElseThrow(() -> new IllegalArgumentException("Account not found"));
        accountUpdated.setBalance(Money.fromDTO(accountDTO.getBalance()));
        
        // Check account type, cast and update
        if (accountUpdated instanceof CreditAccount) {
            var creditAccountUpdated = (CreditAccount) accountUpdated;
            creditAccountRepository.save(creditAccountUpdated);
        } else if (accountUpdated instanceof CurrentCheckingAccount) {
            var currentCheckingAccountUpdated = (CurrentCheckingAccount) accountUpdated;
            currentCheckingAccountRepository.save(currentCheckingAccountUpdated);
        } else if (accountUpdated instanceof CurrentSavingsAccount) {
            var currentSavingsAccountUpdated = (CurrentSavingsAccount) accountUpdated;
            currentSavingsAccountRepository.save(currentSavingsAccountUpdated);
        } else if (accountUpdated instanceof CurrentStudentCheckingAccount) {
            var currentStudentCheckingAccountUpdated = (CurrentStudentCheckingAccount) accountUpdated;
            currentStudentCheckingAccountRepository.save(currentStudentCheckingAccountUpdated);
        }

        return AccountDTO.fromEntity(accountUpdated);
    }

    @Override
    public void delete(String iban) {
        accountRepository.deleteById(iban);
    }

    @Override
    public List<Account> findAllAccounts() {
        return accountRepository.findAll();
    }
}
