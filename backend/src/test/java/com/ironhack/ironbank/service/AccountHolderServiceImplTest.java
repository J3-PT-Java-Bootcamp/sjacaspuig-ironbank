package com.ironhack.ironbank.service;

import com.ironhack.ironbank.dto.AccountHolderDTO;
import com.ironhack.ironbank.dto.AddressDTO;
import lombok.RequiredArgsConstructor;
import net.datafaker.Faker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RequiredArgsConstructor
class AccountHolderServiceImplTest {

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
        var repositorySize = accountHolderService.findAll().size();
        assertTrue(repositorySize > 0);
    }

    @Test
    void findById() throws Exception {
        var repositoryAccountHolder = accountHolderService.findAll().get(0);
        var account = accountHolderService.findById(repositoryAccountHolder.getId());
        assertEquals(repositoryAccountHolder.getId(), account.getId());
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

        var accountHolder = accountHolderService.create(accountHolderDTO);
        assertEquals(accountHolderDTO.getEmail(), accountHolder.getAccountHolder().getEmail());
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
        var accountHolder = accountHolderService.create(accountHolderDTO);
        assertEquals(409, accountHolder.getStatus());
    }
}