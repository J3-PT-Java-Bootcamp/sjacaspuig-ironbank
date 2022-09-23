package com.ironhack.ironbank.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ironhack.ironbank.dto.*;
import com.ironhack.ironbank.enums.AccountStatus;
import com.ironhack.ironbank.repository.AccountRepository;
import com.ironhack.ironbank.service.AccountHolderService;
import com.ironhack.ironbank.service.CurrentCheckingAccountService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


import javax.servlet.Filter;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class AccountControllerTest {

    @Autowired
    WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    CurrentCheckingAccountService CheckingAccountService;
    @Autowired
    AccountHolderService accountHolderService;

    @Autowired
    private Filter springSecurityFilterChain;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).addFilters(springSecurityFilterChain).build();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void findAll() throws Exception {
        var repositorySize = accountRepository.findAll().size();
        var result = mockMvc.perform(get("/accounts")).andExpect(status().isOk()).andReturn();
        var accounts = objectMapper.readValue(result.getResponse().getContentAsString(), Object[].class);
        assertEquals(repositorySize, accounts.length);
    }

    @Test
    void findById() throws Exception {
        var repositoryAccount = accountRepository.findAll().get(0);
        var result = mockMvc.perform(get("/accounts/" + repositoryAccount.getIban())).andExpect(status().isOk()).andReturn();
        var account =  objectMapper.readValue(result.getResponse().getContentAsString(), Object.class);

        // Cast to a checking, saving, current o credit account
        if (account instanceof CurrentCheckingAccountDTO) {
            var accountDTO = (CurrentCheckingAccountDTO) account;
            assertEquals(repositoryAccount.getIban(), accountDTO.getIban());
        } else if (account instanceof CurrentSavingsAccountDTO) {
            var accountDTO = (CurrentSavingsAccountDTO) account;
            assertEquals(repositoryAccount.getIban(), accountDTO.getIban());
        } else if (account instanceof CurrentStudentCheckingAccountDTO) {
            var accountDTO = (CurrentStudentCheckingAccountDTO) account;
            assertEquals(repositoryAccount.getIban(), accountDTO.getIban());
        } else if (account instanceof CreditAccountDTO) {
            var accountDTO = (CreditAccountDTO) account;
            assertEquals(repositoryAccount.getIban(), accountDTO.getIban());
        }
    }

    @Test
    void delete() throws Exception {
        var repositoryAccount = accountRepository.findAll().get(0);
        mockMvc.perform(MockMvcRequestBuilders.delete("/accounts/" + repositoryAccount.getIban())).andExpect(status().isNoContent()).andReturn();
        assertFalse(accountRepository.findById(repositoryAccount.getIban()).isPresent());
        accountRepository.save(repositoryAccount);
    }

    @Test
    void changeStatus() throws Exception {
        var repositoryCheckingAccount = CheckingAccountService.findAll().get(0);
        var newStatus = repositoryCheckingAccount.getStatus() == AccountStatus.ACTIVE ? AccountStatus.FROZEN : AccountStatus.ACTIVE;
        AccountStatusDTO accountStatusDTO = new AccountStatusDTO(repositoryCheckingAccount.getIban(), newStatus);
        var result = mockMvc.perform(patch("/accounts/change-status/" + repositoryCheckingAccount.getIban()).content(objectMapper.writeValueAsString(accountStatusDTO)).contentType("application/json")).andExpect(status().isOk()).andReturn();
        var account = objectMapper.readValue(result.getResponse().getContentAsString(), Object.class);

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
        var repositoryAccountHolder = accountHolderService.findAll().get(0);
        var result = mockMvc.perform(get("/accounts/user/" + repositoryAccountHolder.getId())).andExpect(status().isOk()).andReturn();
        var accounts = objectMapper.readValue(result.getResponse().getContentAsString(), Object[].class);

        // Cast to a checking, saving, current o credit account
        if (accounts[0] instanceof CurrentCheckingAccountDTO) {
            var firstAccount = (CurrentCheckingAccountDTO) accounts[0];
            assertTrue(repositoryAccountHolder.getId() == firstAccount.getPrimaryOwner() || repositoryAccountHolder.getId() == firstAccount.getSecondaryOwner());
        } else if (accounts[0] instanceof CurrentSavingsAccountDTO) {
            var firstAccount = (CurrentSavingsAccountDTO) accounts[0];
            assertTrue(repositoryAccountHolder.getId() == firstAccount.getPrimaryOwner() || repositoryAccountHolder.getId() == firstAccount.getSecondaryOwner());
        } else if (accounts[0] instanceof CurrentCheckingAccountDTO) {
            var firstAccount = (CurrentCheckingAccountDTO) accounts[0];
            assertTrue(repositoryAccountHolder.getId() == firstAccount.getPrimaryOwner() || repositoryAccountHolder.getId() == firstAccount.getSecondaryOwner());
        } else if (accounts[0] instanceof CurrentCheckingAccountDTO) {
            var firstAccount = (CurrentCheckingAccountDTO) accounts[0];
            assertTrue(repositoryAccountHolder.getId() == firstAccount.getPrimaryOwner() || repositoryAccountHolder.getId() == firstAccount.getSecondaryOwner());
        }

    }
}