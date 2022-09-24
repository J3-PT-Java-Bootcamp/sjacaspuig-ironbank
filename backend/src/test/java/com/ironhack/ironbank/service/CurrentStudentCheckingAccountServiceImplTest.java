package com.ironhack.ironbank.service;

import com.ironhack.ironbank.dto.CurrentStudentCheckingAccountDTO;
import com.ironhack.ironbank.dto.MoneyDTO;
import com.ironhack.ironbank.model.account.CurrentStudentCheckingAccount;
import com.ironhack.ironbank.repository.CurrentStudentCheckingAccountRepository;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RequiredArgsConstructor
class CurrentStudentCheckingAccountServiceImplTest {

    @Autowired
    CurrentStudentCheckingAccountService currentStudentCheckingAccountService;
    @Autowired
    AccountHolderService accountHolderService;
    @Autowired
    CurrentStudentCheckingAccountRepository currentStudentCheckingAccountRepository;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void findAll() throws Exception {
        var repositorySize = currentStudentCheckingAccountService.findAll().size();
        assertTrue(repositorySize > 0);
    }

    @Test
    void findByIban() throws Exception {
        var repositoryAccount = currentStudentCheckingAccountService.findAll().get(0);
        var account = currentStudentCheckingAccountService.findByIban(repositoryAccount.getIban());
        assertEquals(repositoryAccount.getIban(), account.getIban());
    }

    @Test
    void update() throws Exception {
        var repositoryAccount = currentStudentCheckingAccountService.findAll().get(0);
        var newBalanceDTO = new MoneyDTO();
        newBalanceDTO.setAmount(repositoryAccount.getBalance().getAmount().add(new BigDecimal(100)));
        repositoryAccount.setBalance(newBalanceDTO);
        var account = currentStudentCheckingAccountService.update(repositoryAccount.getIban(), repositoryAccount);
        assertEquals(newBalanceDTO.getAmount(), account.getBalance().getAmount());
    }

    @Test
    void delete() throws Exception {
        var repositorySize = currentStudentCheckingAccountService.findAll().size();
        var repositoryAccount = currentStudentCheckingAccountService.findAll().get(0);
        currentStudentCheckingAccountService.delete(repositoryAccount.getIban());
        assertEquals(repositorySize - 1, currentStudentCheckingAccountService.findAll().size());
    }
}