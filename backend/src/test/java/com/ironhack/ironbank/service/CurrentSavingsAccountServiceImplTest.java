package com.ironhack.ironbank.service;

import com.ironhack.ironbank.constants.AccountConstants;
import com.ironhack.ironbank.dto.CurrentSavingsAccountDTO;
import com.ironhack.ironbank.dto.MoneyDTO;
import com.ironhack.ironbank.repository.CurrentSavingsAccountRepository;
import lombok.RequiredArgsConstructor;
import net.datafaker.Faker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RequiredArgsConstructor
class CurrentSavingsAccountServiceImplTest {

    @Autowired
    CurrentSavingsAccountService currentSavingsAccountService;
    @Autowired
    AccountHolderService accountHolderService;
    @Autowired
    CurrentSavingsAccountRepository currentSavingsAccountRepository;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void findAll() throws Exception {
        var repositorySize = currentSavingsAccountService.findAll().size();
        assertTrue(repositorySize > 0);
    }

    @Test
    void findByIban() throws Exception {
        var repositoryAccount = currentSavingsAccountService.findAll().get(0);
        var account = currentSavingsAccountService.findByIban(repositoryAccount.getIban());
        assertEquals(repositoryAccount.getIban(), account.getIban());
    }

    @Test
    void create() throws Exception {
        Faker faker = new Faker();

        var savingsAccountDTO = new CurrentSavingsAccountDTO();

        var balance = new MoneyDTO();
        balance.setAmount(AccountConstants.SAVINGS_ACCOUNT_DEFAULT_MINIMUM_BALANCE.getAmount());
        savingsAccountDTO.setBalance(balance);
        savingsAccountDTO.setSecretKey(faker.internet().password());
        var accountHolder = accountHolderService.findAll().get(faker.number().numberBetween(0, accountHolderService.findAll().size() - 1));
        savingsAccountDTO.setPrimaryOwner(accountHolder.getId());
        var account = currentSavingsAccountService.create(savingsAccountDTO);
        assertTrue(account.getIban() != null);
    }

    @Test
    void update() throws Exception {
        var repositoryAccount = currentSavingsAccountService.findAll().get(0);
        var newBalance = repositoryAccount.getBalance().getAmount().add(repositoryAccount.getBalance().getAmount());
        var newBalanceDTO = new MoneyDTO();
        newBalanceDTO.setAmount(newBalance);
        repositoryAccount.setBalance(newBalanceDTO);
        var account = currentSavingsAccountService.update(repositoryAccount.getIban(), repositoryAccount);
        assertEquals(newBalanceDTO.getAmount(), account.getBalance().getAmount());

    }

    @Test
    void delete() throws Exception {
        var repositorySize = currentSavingsAccountRepository.findAll().size();
        var repositoryAccount = currentSavingsAccountRepository.findAll().get(0);
        currentSavingsAccountService.delete(repositoryAccount.getIban());
        assertEquals(repositorySize - 1, currentSavingsAccountRepository.findAll().size());
    }
}