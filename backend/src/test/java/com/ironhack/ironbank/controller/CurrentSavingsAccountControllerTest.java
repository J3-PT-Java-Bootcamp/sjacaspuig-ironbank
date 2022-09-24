package com.ironhack.ironbank.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ironhack.ironbank.dto.CurrentSavingsAccountDTO;
import com.ironhack.ironbank.dto.MoneyDTO;
import com.ironhack.ironbank.repository.CurrentSavingsAccountRepository;
import com.ironhack.ironbank.service.AccountHolderService;
import com.ironhack.ironbank.service.CurrentSavingsAccountService;
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
class CurrentSavingsAccountControllerTest {

    @Autowired
    WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    CurrentSavingsAccountService currentSavingsAccountService;
    @Autowired
    CurrentSavingsAccountRepository currentSavingsAccountRepository;
    @Autowired
    AccountHolderService accountHolderService;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void findAll() throws Exception {
        var repositorySize = currentSavingsAccountService.findAll().size();
        var response = mockMvc.perform(get("/saving-accounts")).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        var accounts = objectMapper.readValue(response, CurrentSavingsAccountDTO[].class);
        assertEquals(repositorySize, accounts.length);
    }

    @Test
    void findById() throws Exception {
        var repositoryAccount = currentSavingsAccountService.findAll().get(0);
        var response = mockMvc.perform(get("/saving-accounts/" + repositoryAccount.getIban())).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        var account = objectMapper.readValue(response, CurrentSavingsAccountDTO.class);
        assertEquals(repositoryAccount.getIban(), account.getIban());
    }

    @Test
    void create() throws Exception {
        Faker faker = new Faker();

        var savingsAccountDTO = new CurrentSavingsAccountDTO();

        var balance = new MoneyDTO();
        balance.setAmount(new BigDecimal(faker.number().randomNumber()));
        savingsAccountDTO.setBalance(balance);
        savingsAccountDTO.setSecretKey(faker.internet().password());
        var accountHolder = accountHolderService.findAll().get(faker.number().numberBetween(0, accountHolderService.findAll().size() - 1));
        savingsAccountDTO.setPrimaryOwner(accountHolder.getId());
        var response = mockMvc.perform(post("/saving-accounts").content(objectMapper.writeValueAsString(savingsAccountDTO)).contentType("application/json")).andExpect(status().isCreated()).andReturn().getResponse().getContentAsString();
        var account = objectMapper.readValue(response, CurrentSavingsAccountDTO.class);
        assertTrue(account.getIban() != null);
    }

    @Test
    void update() throws Exception {
        var repositoryAccount = currentSavingsAccountService.findAll().get(0);
        var newBalance = repositoryAccount.getBalance().getAmount().add(repositoryAccount.getBalance().getAmount());
        var newBalanceDTO = new MoneyDTO();
        newBalanceDTO.setAmount(newBalance);
        repositoryAccount.setBalance(newBalanceDTO);
        var response = mockMvc.perform(put("/saving-accounts/" + repositoryAccount.getIban()).content(objectMapper.writeValueAsString(repositoryAccount)).contentType("application/json")).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();
        var account = objectMapper.readValue(response, CurrentSavingsAccountDTO.class);
        assertEquals(newBalanceDTO.getAmount(), account.getBalance().getAmount());

    }

    @Test
    void delete() throws Exception {
        var repositorySize = currentSavingsAccountRepository.findAll().size();
        var repositoryAccount = currentSavingsAccountRepository.findAll().get(0);
        mockMvc.perform(MockMvcRequestBuilders.delete("/saving-accounts/" + repositoryAccount.getIban())).andExpect(status().isNoContent());
        assertEquals(repositorySize - 1, currentSavingsAccountRepository.findAll().size());
    }
}