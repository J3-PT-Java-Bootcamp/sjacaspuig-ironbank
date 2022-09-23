package com.ironhack.ironbank.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ironhack.ironbank.dto.AdminDTO;
import com.ironhack.ironbank.repository.AdminRepository;
import com.ironhack.ironbank.service.AdminService;
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
class AdminControllerTest {

    @Autowired
    WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    AdminRepository adminRepository;
    @Autowired
    AdminService adminService;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void create_ok() throws Exception {
        Faker faker = new Faker();
        var adminDTO = new AdminDTO();
        adminDTO.setFirstName(faker.name().firstName());
        adminDTO.setLastName(faker.name().lastName());
        adminDTO.setUsername(faker.internet().domainName());
        adminDTO.setEmail(faker.internet().emailAddress());
        adminDTO.setPassword("password");
        var result = mockMvc.perform(post("/admins").content(objectMapper.writeValueAsString(adminDTO)).contentType("application/json")).andExpect(status().isCreated()).andReturn();
        var admin = objectMapper.readValue(result.getResponse().getContentAsString(), AdminDTO.class);
        assertEquals(adminDTO.getEmail(), admin.getEmail());
    }
    
    @Test
    void create_already_exists() throws Exception {
        var adminDTO = adminService.findAll().get(0);
        var adminToCreate = new AdminDTO();
        adminToCreate.setFirstName(adminDTO.getFirstName());
        adminToCreate.setLastName(adminDTO.getLastName());
        adminToCreate.setUsername(adminDTO.getUsername());
        adminToCreate.setEmail(adminDTO.getEmail());
        adminToCreate.setPassword("password");
        var result = mockMvc.perform(post("/admins").content(objectMapper.writeValueAsString(adminDTO)).contentType("application/json")).andExpect(status().isConflict()).andReturn();
        assertEquals(409, result.getResponse().getStatus());
    }

    @Test
    void findAll() throws Exception {
        var repositoryAdmin = adminService.findAll().size();
        var result = mockMvc.perform(get("/admins")).andExpect(status().isOk()).andReturn();
        var admins = objectMapper.readValue(result.getResponse().getContentAsString(), AdminDTO[].class);
        assertEquals(repositoryAdmin, admins.length);
    }

    @Test
    void findById() throws Exception {
        var repositoryAdmin = adminService.findAll().get(0);
        var result = mockMvc.perform(get("/admins/" + repositoryAdmin.getId())).andExpect(status().isOk()).andReturn();  
        var admin = objectMapper.readValue(result.getResponse().getContentAsString(), AdminDTO.class);
        assertEquals(repositoryAdmin.getId(), admin.getId());
    }
}