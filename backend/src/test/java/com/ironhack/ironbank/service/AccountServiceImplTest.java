package com.ironhack.ironbank.service;

import com.ironhack.ironbank.dto.*;
import com.ironhack.ironbank.enums.AccountStatus;
import com.ironhack.ironbank.model.account.*;
import com.ironhack.ironbank.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RequiredArgsConstructor
class AccountServiceImplTest {

    @Autowired
    AccountService accountService;
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    CurrentCheckingAccountService currentCheckingAccountService;
    @Autowired
    AccountHolderService accountHolderService;

    @Test
    void findAll() throws Exception {
        var repositorySize = accountService.findAll().size();
        assertTrue(repositorySize > 0);
    }

    @Test
    void findById() throws Exception {
        var repositoryAccount = accountService.findAll().get(0);
        var account = accountService.findById(repositoryAccount.getIban()).get();

        // Cast to a checking, saving, current o credit account
        if (account instanceof CurrentCheckingAccount) {
            var accountDTO = (CurrentCheckingAccount) account;
            assertEquals(repositoryAccount.getIban(), accountDTO.getIban());
        } else if (account instanceof CurrentSavingsAccount) {
            var accountDTO = (CurrentSavingsAccount) account;
            assertEquals(repositoryAccount.getIban(), accountDTO.getIban());
        } else if (account instanceof CurrentStudentCheckingAccount) {
            var accountDTO = (CurrentStudentCheckingAccount) account;
            assertEquals(repositoryAccount.getIban(), accountDTO.getIban());
        } else if (account instanceof CreditAccount) {
            var accountDTO = (CreditAccount) account;
            assertEquals(repositoryAccount.getIban(), accountDTO.getIban());
        }
    }

    @Test
    void changeStatus() throws Exception {
        var repositoryCheckingAccount = currentCheckingAccountService.findAll().get(0);
        var newStatus = repositoryCheckingAccount.getStatus() == AccountStatus.ACTIVE ? AccountStatus.FROZEN : AccountStatus.ACTIVE;
        AccountStatusDTO accountStatusDTO = new AccountStatusDTO(repositoryCheckingAccount.getIban(), newStatus);
        var account = accountService.changeStatus(repositoryCheckingAccount.getIban(), accountStatusDTO);

        if (account instanceof CurrentCheckingAccountDTO) {
            var accountDTO = (CurrentCheckingAccountDTO) account;
            assertEquals(newStatus, accountDTO.getStatus());
        } else if (account instanceof CurrentSavingsAccountDTO) {
            var accountDTO = (CurrentSavingsAccountDTO) account;
            assertEquals(newStatus, accountDTO.getStatus());
        } else if (account instanceof CurrentStudentCheckingAccountDTO) {
            var accountDTO = (CurrentStudentCheckingAccountDTO) account;
            assertEquals(newStatus, accountDTO.getStatus());
        } else if (account instanceof CreditAccountDTO) {
            var accountDTO = (CreditAccountDTO) account;
            assertEquals(newStatus, false);
        }
    }

    @Test
    void findByAccountHolderId() throws Exception {
        Object account = null;
        AccountHolderDTO repositoryAccountHolder = null;
        var accountHolders = accountHolderService.findAll();

        for (int i = 0; i < accountHolders.size(); i++) {
            repositoryAccountHolder = accountHolders.get(i);
            var accounts = accountService.findByAccountHolderId(repositoryAccountHolder.getId());
            if (accounts.size() > 0) {
                account = accounts.get(0);
                break;
            }
        }

        // Cast to a checking, saving, current o credit account
        if (account instanceof CurrentCheckingAccountDTO) {
            var firstAccount = (CurrentCheckingAccountDTO) account;
            assertTrue(Objects.equals(repositoryAccountHolder.getId(), firstAccount.getPrimaryOwner()) || Objects.equals(repositoryAccountHolder.getId(), firstAccount.getSecondaryOwner()));
        } else if (account instanceof CurrentSavingsAccountDTO) {
            var firstAccount = (CurrentSavingsAccountDTO) account;
            assertTrue(Objects.equals(repositoryAccountHolder.getId(), firstAccount.getPrimaryOwner()) || Objects.equals(repositoryAccountHolder.getId(), firstAccount.getSecondaryOwner()));
        } else if (account instanceof CurrentStudentCheckingAccountDTO) {
            var firstAccount = (CurrentStudentCheckingAccountDTO) account;
            assertTrue(Objects.equals(repositoryAccountHolder.getId(), firstAccount.getPrimaryOwner()) || Objects.equals(repositoryAccountHolder.getId(), firstAccount.getSecondaryOwner()));
        } else if (account instanceof CreditAccountDTO) {
            var firstAccount = (CreditAccountDTO) account;
            assertTrue(Objects.equals(repositoryAccountHolder.getId(), firstAccount.getPrimaryOwner()) || Objects.equals(repositoryAccountHolder.getId(), firstAccount.getSecondaryOwner()));
        }

    }
}