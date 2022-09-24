package com.ironhack.ironbank.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ironhack.ironbank.dto.CurrentStudentCheckingAccountDTO;
import com.ironhack.ironbank.dto.MoneyDTO;
import com.ironhack.ironbank.repository.CurrentStudentCheckingAccountRepository;
import com.ironhack.ironbank.service.AccountHolderService;
import com.ironhack.ironbank.service.CurrentStudentCheckingAccountService;
import lombok.RequiredArgsConstructor;
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
class CurrentStudentCheckingAccountControllerTest {

    @Autowired
    WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    CurrentStudentCheckingAccountService currentStudentCheckingAccountService;
    @Autowired
    CurrentStudentCheckingAccountRepository currentStudentCheckingAccountRepository;
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
        var repositorySize = currentStudentCheckingAccountService.findAll().size();
        var result = mockMvc.perform(get("/student-checking-accounts")).andExpect(status().isOk()).andReturn();
        var accounts = objectMapper.readValue(result.getResponse().getContentAsString(), CurrentStudentCheckingAccountDTO[].class);
        assertEquals(repositorySize, accounts.length);
    }

    @Test
    void findById() throws Exception {
        var repositoryAccount = currentStudentCheckingAccountService.findAll().get(0);
        var result = mockMvc.perform(get("/student-checking-accounts/" + repositoryAccount.getIban())).andExpect(status().isOk()).andReturn();
        var account = objectMapper.readValue(result.getResponse().getContentAsString(), CurrentStudentCheckingAccountDTO.class);
        assertEquals(repositoryAccount.getIban(), account.getIban());
    }

    @Test
    void update() throws Exception {
        var repositoryAccount = currentStudentCheckingAccountService.findAll().get(0);
        var newBalanceDTO = new MoneyDTO();
        newBalanceDTO.setAmount(repositoryAccount.getBalance().getAmount().add(new BigDecimal(100)));
        repositoryAccount.setBalance(newBalanceDTO);
        var result = mockMvc.perform(put("/student-checking-accounts/" + repositoryAccount.getIban())
                .content(objectMapper.writeValueAsString(repositoryAccount))
                .contentType("application/json"))
                .andExpect(status().isOk()).andReturn();
        var account = objectMapper.readValue(result.getResponse().getContentAsString(), CurrentStudentCheckingAccountDTO.class);
        assertEquals(newBalanceDTO.getAmount(), account.getBalance().getAmount());
    }

    @Test
    void delete() throws Exception {
        var repositorySize = currentStudentCheckingAccountService.findAll().size();
        var repositoryAccount = currentStudentCheckingAccountService.findAll().get(0);
        mockMvc.perform(MockMvcRequestBuilders.delete("/student-checking-accounts/" + repositoryAccount.getIban())).andExpect(status().isNoContent());
        assertEquals(repositorySize - 1, currentStudentCheckingAccountService.findAll().size());
    }
}