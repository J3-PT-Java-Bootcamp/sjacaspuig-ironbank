package com.ironhack.ironbank.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ironhack.ironbank.dto.AccountHolderDTO;
import com.ironhack.ironbank.dto.AddressDTO;
import com.ironhack.ironbank.repository.AccountHolderRepository;
import com.ironhack.ironbank.service.AccountHolderService;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class AccountHolderControllerTest {

    @Autowired
    WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

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
        var repositorySize = accountHolderService.findAll().size();
        var result = mockMvc.perform(get("/account-holders")).andExpect(status().isOk()).andReturn();
        var accountHolders = objectMapper.readValue(result.getResponse().getContentAsString(), AccountHolderDTO[].class);
        assertEquals(repositorySize, accountHolders.length);
    }

    @Test
    void findById() throws Exception {
        var repositoryAccountHolder = accountHolderService.findAll().get(0);
        var result = mockMvc.perform(get("/account-holders/" + repositoryAccountHolder.getId())).andExpect(status().isOk()).andReturn();
        var accountHolder = objectMapper.readValue(result.getResponse().getContentAsString(), AccountHolderDTO.class);
        assertEquals(repositoryAccountHolder.getId(), accountHolder.getId());
    }

    @Test
    void create() throws Exception {
        Faker faker = new Faker();
        var accountHolderDTO = new AccountHolderDTO();
        accountHolderDTO.setFirstName(faker.name().firstName());
        accountHolderDTO.setLastName(faker.name().lastName());
        accountHolderDTO.setUsername(faker.internet().domainName());
        accountHolderDTO.setEmail(faker.internet().emailAddress());
        accountHolderDTO.setBirthDate(faker.date().birthday(23, 26));

        var addressDTO = new AddressDTO();
        addressDTO.setStreet(faker.address().streetAddress());
        addressDTO.setNumber(faker.address().buildingNumber());
        addressDTO.setCity(faker.address().city());
        addressDTO.setCountry(faker.address().country());
        addressDTO.setPostalCode(faker.address().zipCode());
        accountHolderDTO.setPrimaryAddress(addressDTO);

        accountHolderDTO.setPassword("password");

        var result = mockMvc.perform(post("/account-holders").content(objectMapper.writeValueAsString(accountHolderDTO)).contentType("application/json")).andExpect(status().isCreated()).andReturn();
        var accountHolder = objectMapper.readValue(result.getResponse().getContentAsString(), AccountHolderDTO.class);
        assertEquals(accountHolderDTO.getEmail(), accountHolder.getEmail());
    }

    @Test
    void create_already_exists() throws Exception {
        var accountHolderDTO = accountHolderService.findAll().get(0);
        var accountHolderToCreate = new AccountHolderDTO();
        accountHolderToCreate.setFirstName(accountHolderDTO.getFirstName());
        accountHolderToCreate.setLastName(accountHolderDTO.getLastName());
        accountHolderToCreate.setUsername(accountHolderDTO.getUsername());
        accountHolderToCreate.setEmail(accountHolderDTO.getEmail());
        accountHolderToCreate.setBirthDate(accountHolderDTO.getBirthDate());
        accountHolderToCreate.setPrimaryAddress(accountHolderDTO.getPrimaryAddress());
        accountHolderToCreate.setPassword("password");
        var result = mockMvc.perform(post("/account-holders").content(objectMapper.writeValueAsString(accountHolderToCreate)).contentType("application/json")).andExpect(status().isConflict()).andReturn();
        assertEquals(409, result.getResponse().getStatus());
    }
}