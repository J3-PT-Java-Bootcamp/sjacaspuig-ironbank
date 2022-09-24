package com.ironhack.ironbank.service;

import com.ironhack.ironbank.dto.ThirdPartyDTO;
import lombok.RequiredArgsConstructor;
import net.datafaker.Faker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RequiredArgsConstructor
class ThirdPartyServiceImplTest {

    @Autowired
    ThirdPartyService thirdPartyService;

    @Test
    void findAll() throws Exception {
        var repositorySize = thirdPartyService.findAll().size();
        assertTrue(repositorySize > 0);
    }

    @Test
    void findById() throws Exception {
        var repositoryThirdParty = thirdPartyService.findAll().get(0);
        var thirdParty = thirdPartyService.findById(repositoryThirdParty.getId());
        assertEquals(repositoryThirdParty.getId(), thirdParty.getId());
    }

    @Test
    void create() throws Exception {
        Faker faker = new Faker();
        var thirdPartyDTO = new ThirdPartyDTO();
        thirdPartyDTO.setFirstName(faker.name().firstName());
        thirdPartyDTO.setLastName(faker.name().lastName());
        thirdPartyDTO.setHashedKey(faker.hashing().sha256());
        var thirdParty = thirdPartyService.create(thirdPartyDTO);
        assertEquals(thirdPartyDTO.getFirstName(), thirdParty.getThirdParty().getFirstName());
    }

    @Test
    void update() throws Exception {
        var repositoryThirdParty = thirdPartyService.findAll().get(0);
        var thirdPartyDTO = new ThirdPartyDTO();
        thirdPartyDTO.setFirstName(repositoryThirdParty.getLastName());
        thirdPartyDTO.setLastName(repositoryThirdParty.getFirstName());
        thirdPartyDTO.setHashedKey(repositoryThirdParty.getHashedKey());
        var thirdParty = thirdPartyService.update(repositoryThirdParty.getId(), thirdPartyDTO);
        assertEquals(repositoryThirdParty.getLastName(), thirdParty.getFirstName());
    }

    @Test
    void delete() throws Exception {
        var repositoryThirdParty = thirdPartyService.findAll().get(0);
        thirdPartyService.delete(repositoryThirdParty.getId());
        assertFalse(thirdPartyService.findAll().contains(repositoryThirdParty));
    }
}