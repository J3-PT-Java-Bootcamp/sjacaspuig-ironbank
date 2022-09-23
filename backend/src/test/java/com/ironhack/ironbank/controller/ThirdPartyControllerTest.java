package com.ironhack.ironbank.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ironhack.ironbank.dto.ThirdPartyDTO;
import com.ironhack.ironbank.repository.ThirdPartyRepository;
import com.ironhack.ironbank.service.ThirdPartyService;
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
class ThirdPartyControllerTest {

    @Autowired
    WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    ThirdPartyService thirdPartyService;
    @Autowired
    ThirdPartyRepository thirdPartyRepository;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void findAll() throws Exception {
        var repositorySize = thirdPartyService.findAll().size();
        var result = mockMvc.perform(get("/third-parties")).andExpect(status().isOk()).andReturn();
        var thirdParties = objectMapper.readValue(result.getResponse().getContentAsString(), ThirdPartyDTO[].class);
        assertEquals(repositorySize, thirdParties.length);
    }

    @Test
    void findById() throws Exception {
        var repositoryThirdParty = thirdPartyService.findAll().get(0);
        var result = mockMvc.perform(get("/third-parties/" + repositoryThirdParty.getId())).andExpect(status().isOk()).andReturn();
        var thirdParty = objectMapper.readValue(result.getResponse().getContentAsString(), ThirdPartyDTO.class);
        assertEquals(repositoryThirdParty.getId(), thirdParty.getId());
    }

    @Test
    void create() throws Exception {
        Faker faker = new Faker();
        var thirdPartyDTO = new ThirdPartyDTO();
        thirdPartyDTO.setFirstName(faker.name().firstName());
        thirdPartyDTO.setLastName(faker.name().lastName());
        thirdPartyDTO.setHashedKey(faker.hashing().sha256());
        var result = mockMvc.perform(post("/third-parties").content(objectMapper.writeValueAsString(thirdPartyDTO)).contentType("application/json")).andExpect(status().isCreated()).andReturn();
        var thirdParty = objectMapper.readValue(result.getResponse().getContentAsString(), ThirdPartyDTO.class);
        assertEquals(thirdPartyDTO.getFirstName(), thirdParty.getFirstName());
    }

    @Test
    void update() throws Exception {
        var repositoryThirdParty = thirdPartyService.findAll().get(0);
        var thirdPartyDTO = new ThirdPartyDTO();
        thirdPartyDTO.setFirstName(repositoryThirdParty.getLastName());
        thirdPartyDTO.setLastName(repositoryThirdParty.getFirstName());
        thirdPartyDTO.setHashedKey(repositoryThirdParty.getHashedKey());
        var result = mockMvc.perform(put("/third-parties/" + repositoryThirdParty.getId()).content(objectMapper.writeValueAsString(thirdPartyDTO)).contentType("application/json")).andExpect(status().isOk()).andReturn();
        var thirdParty = objectMapper.readValue(result.getResponse().getContentAsString(), ThirdPartyDTO.class);
        assertEquals(repositoryThirdParty.getLastName(), thirdParty.getFirstName());
    }

    @Test
    void delete() throws Exception {
        var repositoryThirdParty = thirdPartyService.findAll().get(0);
        mockMvc.perform(MockMvcRequestBuilders.delete("/third-parties/" + repositoryThirdParty.getId())).andExpect(status().isNoContent());
        assertFalse(thirdPartyRepository.findById(repositoryThirdParty.getId()).isPresent());
    }

}