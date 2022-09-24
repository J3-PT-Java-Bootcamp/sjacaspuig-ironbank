package com.ironhack.ironbank.service;

import com.ironhack.ironbank.constants.AccountConstants;
import com.ironhack.ironbank.dto.CreditAccountDTO;
import com.ironhack.ironbank.dto.MoneyDTO;
import com.ironhack.ironbank.repository.CreditAccountRepository;
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
class CreditAccountServiceImplTest {

    @Autowired
    CreditAccountService creditAccountService;
    @Autowired
    CreditAccountRepository creditAccountRepository;
    @Autowired
    AccountHolderService accountHolderService;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void findAll() throws Exception {
        var repositorySize = creditAccountService.findAll().size();
        assertTrue(repositorySize > 0);
    }

    @Test
    void findByIban() throws Exception {
        var repositoryCreditAccount = creditAccountService.findAll().get(0);
        var creditAccount = creditAccountService.findByIban(repositoryCreditAccount.getIban());
        assertEquals(repositoryCreditAccount.getIban(), creditAccount.getIban());
    }

    @Test
    void create() throws Exception {
        Faker faker = new Faker();
        var creditAccountDTO = new CreditAccountDTO();

        var balance = new MoneyDTO();
        balance.setAmount(new BigDecimal(faker.number().numberBetween(0, 100)));
        creditAccountDTO.setBalance(balance);

        var accountHolder = accountHolderService.findAll().get(faker.number().numberBetween(0, accountHolderService.findAll().size() - 1));
        creditAccountDTO.setPrimaryOwner(accountHolder.getId());

        var creditAccount = creditAccountService.create(creditAccountDTO);
        assertTrue(creditAccount.getIban() != null);
    }

    @Test
    void update() throws Exception {
        var repositoryCreditAccount = creditAccountService.findAll().get(0);
        var newCreditLimit = repositoryCreditAccount.getCreditLimit().getAmount() == AccountConstants.CREDIT_ACCOUNT_MAX_CREDIT_LIMIT.getAmount() ? AccountConstants.CREDIT_ACCOUNT_DEFAULT_CREDIT_LIMIT : AccountConstants.CREDIT_ACCOUNT_MAX_CREDIT_LIMIT;
        var newCreditLimitDTO = MoneyDTO.fromEntity(newCreditLimit);
        repositoryCreditAccount.setCreditLimit(newCreditLimitDTO);
        var creditAccount = creditAccountService.update(repositoryCreditAccount.getIban(), repositoryCreditAccount);
        assertEquals(newCreditLimitDTO.getAmount(), creditAccount.getCreditLimit().getAmount());
    }

    @Test
    void delete() throws Exception {
        var repositoryCreditAccount = creditAccountRepository.findAll().get(0);
        creditAccountService.delete(repositoryCreditAccount.getIban());
        assertFalse(creditAccountRepository.findById(repositoryCreditAccount.getIban()).isPresent());
        creditAccountRepository.save(repositoryCreditAccount);
    }
}