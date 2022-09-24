package com.ironhack.ironbank.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ironhack.ironbank.constants.AccountConstants;
import com.ironhack.ironbank.dto.CreditAccountDTO;
import com.ironhack.ironbank.dto.MoneyDTO;
import com.ironhack.ironbank.model.account.CreditAccount;
import com.ironhack.ironbank.repository.CreditAccountRepository;
import com.ironhack.ironbank.service.AccountHolderService;
import com.ironhack.ironbank.service.CreditAccountService;
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
class CreditAccountControllerTest {

    @Autowired
    WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @Autowired
    CreditAccountRepository creditAccountRepository;
    @Autowired
    CreditAccountService creditAccountService;
    @Autowired
    AccountHolderService accountHolderService;

    @AfterEach
    void tearDown() {
    }

    @Test
    void findAll() throws Exception {
        var repositorySize = creditAccountService.findAll().size();
        var response = mockMvc.perform(get("/credit-accounts")).andExpect(status().isOk()).andReturn().getResponse();
        var responseSize = objectMapper.readValue(response.getContentAsString(), CreditAccountDTO[].class).length;
        assertEquals(repositorySize, responseSize);
    }

    @Test
    void findById() throws Exception {
        var repositoryCreditAccount = creditAccountService.findAll().get(0);
        var response = mockMvc.perform(get("/credit-accounts/" + repositoryCreditAccount.getIban())).andExpect(status().isOk()).andReturn().getResponse();
        var responseCreditAccount = objectMapper.readValue(response.getContentAsString(), CreditAccountDTO.class);
        assertEquals(repositoryCreditAccount.getIban(), responseCreditAccount.getIban());
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

        var response = mockMvc.perform(post("/credit-accounts").content(objectMapper.writeValueAsString(creditAccountDTO)).contentType("application/json")).andExpect(status().isCreated()).andReturn().getResponse();
        var responseCreditAccount = objectMapper.readValue(response.getContentAsString(), CreditAccountDTO.class);
        assertTrue(responseCreditAccount.getIban() != null);
    }

    @Test
    void update() throws Exception {
        var repositoryCreditAccount = creditAccountService.findAll().get(0);
        var newCreditLimit = repositoryCreditAccount.getCreditLimit().getAmount() == AccountConstants.CREDIT_ACCOUNT_MAX_CREDIT_LIMIT.getAmount() ? AccountConstants.CREDIT_ACCOUNT_DEFAULT_CREDIT_LIMIT : AccountConstants.CREDIT_ACCOUNT_MAX_CREDIT_LIMIT;
        var newCreditLimitDTO = MoneyDTO.fromEntity(newCreditLimit);
        repositoryCreditAccount.setCreditLimit(newCreditLimitDTO);
        var response = mockMvc.perform(put("/credit-accounts/" + repositoryCreditAccount.getIban()).content(objectMapper.writeValueAsString(repositoryCreditAccount)).contentType("application/json")).andExpect(status().isOk()).andReturn().getResponse();
        var responseCreditAccount = objectMapper.readValue(response.getContentAsString(), CreditAccountDTO.class);
        assertEquals(newCreditLimitDTO.getAmount(), responseCreditAccount.getCreditLimit().getAmount());
    }

    @Test
    void delete() throws Exception {
        var repositoryCreditAccount = creditAccountRepository.findAll().get(0);
        mockMvc.perform(MockMvcRequestBuilders.delete("/credit-accounts/" + repositoryCreditAccount.getIban())).andExpect(status().isNoContent());
        assertFalse(creditAccountRepository.findById(repositoryCreditAccount.getIban()).isPresent());
        creditAccountRepository.save(repositoryCreditAccount);
    }
}