package com.ironhack.ironbank.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ironhack.ironbank.constants.AccountConstants;
import com.ironhack.ironbank.dto.AccountHolderDTO;
import com.ironhack.ironbank.dto.AddressDTO;
import com.ironhack.ironbank.dto.CurrentCheckingAccountDTO;
import com.ironhack.ironbank.dto.MoneyDTO;
import com.ironhack.ironbank.model.Address;
import com.ironhack.ironbank.model.user.AccountHolder;
import com.ironhack.ironbank.repository.AccountHolderRepository;
import com.ironhack.ironbank.repository.CurrentCheckingAccountRepository;
import com.ironhack.ironbank.service.AccountHolderService;
import com.ironhack.ironbank.service.CurrentCheckingAccountService;
import com.ironhack.ironbank.utils.DateService;
import lombok.RequiredArgsConstructor;
import net.datafaker.Faker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class CurrentCheckingAccountControllerTest {

    @Autowired
    WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    CurrentCheckingAccountRepository currentCheckingAccountRepository;
    @Autowired
    CurrentCheckingAccountService currentCheckingAccountService;
    @Autowired
    AccountHolderService accountHolderService;
    @Autowired
    AccountHolderRepository accountHolderRepository;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void findAll() throws Exception {
        var repositorySize = currentCheckingAccountService.findAll().size();
        var result = mockMvc.perform(get("/checking-accounts")).andExpect(status().isOk()).andReturn();
        var account = objectMapper.readValue(result.getResponse().getContentAsString(), CurrentCheckingAccountDTO[].class);
        assertEquals(repositorySize, account.length);
    }

    @Test
    void findById() throws Exception {
        var repositoryAccount = currentCheckingAccountService.findAll().get(0);
        var result = mockMvc.perform(get("/checking-accounts/" + repositoryAccount.getIban())).andExpect(status().isOk()).andReturn();
        var account = objectMapper.readValue(result.getResponse().getContentAsString(), CurrentCheckingAccountDTO.class);
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

        var result = mockMvc.perform(post("/checking-accounts")
                .content(objectMapper.writeValueAsString(checkingAccountDTO))
                .contentType("application/json"))
                .andExpect(status().isCreated()).andReturn();
        var account = objectMapper.readValue(result.getResponse().getContentAsString(), CurrentCheckingAccountDTO.class);
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
        var result = mockMvc.perform(put("/checking-accounts/" + repositoryAccount.getIban()).content(objectMapper.writeValueAsString(repositoryAccount)).contentType("application/json")).andExpect(status().isOk()).andReturn();
        var account = objectMapper.readValue(result.getResponse().getContentAsString(), CurrentCheckingAccountDTO.class);
        assertEquals(newBalanceDTO.getAmount(), account.getBalance().getAmount());
    }

    @Test
    void delete() throws Exception {
        var repositoryAccount = currentCheckingAccountRepository.findAll().get(0);
        mockMvc.perform(MockMvcRequestBuilders.delete("/checking-accounts/" + repositoryAccount.getIban())).andExpect(status().isNoContent());
        assertFalse(currentCheckingAccountRepository.findById(repositoryAccount.getIban()).isPresent());
        currentCheckingAccountRepository.save(repositoryAccount);
    }
}