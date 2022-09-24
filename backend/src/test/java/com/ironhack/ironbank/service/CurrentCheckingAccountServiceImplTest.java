package com.ironhack.ironbank.service;

import com.ironhack.ironbank.constants.AccountConstants;
import com.ironhack.ironbank.dto.AccountHolderDTO;
import com.ironhack.ironbank.dto.CurrentCheckingAccountDTO;
import com.ironhack.ironbank.dto.MoneyDTO;
import com.ironhack.ironbank.model.Address;
import com.ironhack.ironbank.model.user.AccountHolder;
import com.ironhack.ironbank.repository.AccountHolderRepository;
import com.ironhack.ironbank.repository.CurrentCheckingAccountRepository;
import com.ironhack.ironbank.utils.DateService;
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
class CurrentCheckingAccountServiceImplTest {

    @Autowired
    CurrentCheckingAccountService currentCheckingAccountService;
    @Autowired
    CurrentCheckingAccountRepository currentCheckingAccountRepository;
    @Autowired
    AccountHolderRepository accountHolderRepository;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void findAll() throws Exception {
        var repositorySize = currentCheckingAccountService.findAll().size();
        assertTrue(repositorySize > 0);
    }

    @Test
    void findByIban() throws Exception {
        var repositoryAccount = currentCheckingAccountService.findAll().get(0);
        var account = currentCheckingAccountService.findByIban(repositoryAccount.getIban());
        assertEquals(repositoryAccount.getIban(), account.getIban());
    }

    @Test
    void create() throws Exception {
        Faker faker = new Faker();

        var checkingAccountDTO = new CurrentCheckingAccountDTO();

        var balance = new MoneyDTO();
        balance.setAmount(AccountConstants.CHECKING_ACCOUNT_MINIMUM_BALANCE.getAmount());
        checkingAccountDTO.setBalance(balance);
        checkingAccountDTO.setSecretKey(faker.internet().password());

        // Create an account holder older than 24
        var accountHolder = new AccountHolder();
        accountHolder.setFirstName(faker.name().firstName());
        accountHolder.setLastName(faker.name().lastName());
        accountHolder.setUsername(faker.internet().domainName());
        accountHolder.setEmail(faker.internet().emailAddress());
        accountHolder.setBirthDate(DateService.parseDate(faker.date().birthday(30, 50)));
        var address = new Address();
        address.setStreet(faker.address().streetAddress());
        address.setNumber(faker.address().buildingNumber());
        address.setCity(faker.address().city());
        address.setCountry(faker.address().country());
        address.setPostalCode(faker.address().zipCode());
        accountHolder.setPrimaryAddress(address);
        // Faker random uuid
        accountHolder.setId(faker.internet().uuid());

        var accountHolderDTO = AccountHolderDTO.fromEntity(accountHolderRepository.save(accountHolder));

        checkingAccountDTO.setPrimaryOwner(accountHolderDTO.getId());

        var account = currentCheckingAccountService.create(checkingAccountDTO);
        assertTrue(account.getIban() != null);
    }

    @Test
    void update() throws Exception {
        var repositoryAccount = currentCheckingAccountService.findAll().get(0);
        // Update balance to the minimum
        var newBalance = AccountConstants.CHECKING_ACCOUNT_MINIMUM_BALANCE.getAmount() == repositoryAccount.getBalance().getAmount() ? AccountConstants.CHECKING_ACCOUNT_MINIMUM_BALANCE.increaseAmount(new BigDecimal(1000)) : AccountConstants.CHECKING_ACCOUNT_MINIMUM_BALANCE.getAmount();
        var newBalanceDTO = new MoneyDTO();
        newBalanceDTO.setAmount(newBalance);
        repositoryAccount.setBalance(newBalanceDTO);
        var account = currentCheckingAccountService.update(repositoryAccount.getIban(), repositoryAccount);
        assertEquals(newBalanceDTO.getAmount(), account.getBalance().getAmount());
    }

    @Test
    void delete() throws Exception {
        var repositoryAccount = currentCheckingAccountRepository.findAll().get(0);
        currentCheckingAccountService.delete(repositoryAccount.getIban());
        assertFalse(currentCheckingAccountRepository.findById(repositoryAccount.getIban()).isPresent());
        currentCheckingAccountRepository.save(repositoryAccount);
    }
}