package com.ironhack.ironbank.service;

import com.ironhack.ironbank.dto.*;
import com.ironhack.ironbank.model.account.*;
import com.ironhack.ironbank.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final CurrentCheckingAccountService checkingAccountService;
    private final CurrentSavingsAccountService savingsAccountService;
    private final CurrentStudentCheckingAccountService studentCheckingAccountService;
    private final CreditAccountService creditAccountService;

    @Override
    public AccountDTO findByIban(String iban) {
        var account = accountRepository.findById(iban).orElseThrow(() -> new IllegalArgumentException("Account not found"));
        if(account instanceof CurrentCheckingAccount) {
            return checkingAccountService.findByIban(iban);
        } else if(account instanceof CurrentSavingsAccount) {
            return savingsAccountService.findByIban(iban);
        } else if(account instanceof CurrentStudentCheckingAccount) {
            return studentCheckingAccountService.findByIban(iban);
        } else if(account instanceof CreditAccount) {
            return creditAccountService.findByIban(iban);
        } else {
            throw new IllegalArgumentException("Account not found");
        }
    }

    @Override
    public Optional<? extends Account> findById(String iban) {
        var account = accountRepository.findById(iban);
        if(account.isPresent()) {
            if(account.get() instanceof CurrentCheckingAccount) {
                return checkingAccountService.findEntity(iban);
            } else if(account.get() instanceof CurrentSavingsAccount) {
                return savingsAccountService.findEntity(iban);
            } else if(account.get() instanceof CurrentStudentCheckingAccount) {
                return studentCheckingAccountService.findEntity(iban);
            } else if(account.get() instanceof CreditAccount) {
                return creditAccountService.findEntity(iban);
            } else {
                throw new IllegalArgumentException("Account not found");
            }
        } else {
            throw new IllegalArgumentException("Account not found");
        }
    }

    @Override
    public List<AccountDTO> findAll() {
        List<AccountDTO> accountDTOList = new ArrayList<>();
        accountDTOList.addAll(checkingAccountService.findAll());
        accountDTOList.addAll(savingsAccountService.findAll());
        accountDTOList.addAll(studentCheckingAccountService.findAll());
        accountDTOList.addAll(creditAccountService.findAll());
        return accountDTOList;
    }

    @Override
    public void delete(String iban) {
        accountRepository.deleteById(iban);
    }

    @Override
    public List<Account> findAllAccounts() {
        List<Account> accountList = new ArrayList<>();
        accountList.addAll(checkingAccountService.findAllEntities());
        accountList.addAll(savingsAccountService.findAllEntities());
        accountList.addAll(studentCheckingAccountService.findAllEntities());
        accountList.addAll(creditAccountService.findAllEntities());
        return accountList;
    }

    @Override
    public AccountDTO changeStatus(@Valid String iban, @Valid AccountStatusDTO accountStatusDTO) {
        var account = accountRepository.findById(iban).orElseThrow(() -> new IllegalArgumentException("Account not found"));
        if(account instanceof CurrentCheckingAccount) {
            return checkingAccountService.changeStatus(iban, accountStatusDTO);
        } else if(account instanceof CurrentSavingsAccount) {
            return savingsAccountService.changeStatus(iban, accountStatusDTO);
        } else if(account instanceof CurrentStudentCheckingAccount) {
            return studentCheckingAccountService.changeStatus(iban, accountStatusDTO);
        } else {
            throw new IllegalArgumentException("Account is of the type" + account.getAccountType() + "and cannot be frozen");
        }
    }

    @Override
    public List<AccountDTO> findByAccountHolderId(@Valid String id) {
        List<AccountDTO> accountDTOList = new ArrayList<>();
        accountDTOList.addAll(checkingAccountService.findByAccountHolderId(id));
        accountDTOList.addAll(savingsAccountService.findByAccountHolderId(id));
        accountDTOList.addAll(studentCheckingAccountService.findByAccountHolderId(id));
        accountDTOList.addAll(creditAccountService.findByAccountHolderId(id));
        return accountDTOList;
    }
}
