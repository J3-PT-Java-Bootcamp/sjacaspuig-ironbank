package com.ironhack.ironbank.service;

import com.ironhack.ironbank.dto.AdminDTO;
import lombok.RequiredArgsConstructor;
import net.datafaker.Faker;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RequiredArgsConstructor
class AdminServiceImplTest {

    @Autowired
    AdminService adminService;

    @Test
    void create_ok() throws Exception {
        Faker faker = new Faker();
        var adminDTO = new AdminDTO();
        adminDTO.setFirstName(faker.name().firstName());
        adminDTO.setLastName(faker.name().lastName());
        adminDTO.setUsername(faker.internet().domainName());
        adminDTO.setEmail(faker.internet().emailAddress());
        adminDTO.setPassword("password");
        var admin = adminService.create(adminDTO);
        assertEquals(adminDTO.getEmail(), admin.getAdmin().getEmail());
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
        var admin = adminService.create(adminDTO);
        assertEquals(409, admin.getStatus());
    }

    @Test
    void findAll() throws Exception {
        var repositorySize = adminService.findAll().size();
        assertTrue(repositorySize > 0);
    }

    @Test
    void findById() throws Exception {
        var repositoryAdmin = adminService.findAll().get(0);
        var admin = adminService.findById(repositoryAdmin.getId());
        assertEquals(repositoryAdmin.getId(), admin.getId());
    }
}