package com.ironhack.ironbank.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AllArgsConstructor
class AccountControllerTest {

    private MockMvc mockMvc;
    private final WebApplicationContext webApplicationContext;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void findAll() {
    }

    @Test
    void findById() {
    }

    @Test
    void delete() {
    }

    @Test
    void changeStatus() {
    }

    @Test
    void findByAccountHolderId() {
    }
}