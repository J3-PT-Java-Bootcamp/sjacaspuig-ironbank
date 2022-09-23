package com.ironhack.ironbank.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@RequiredArgsConstructor
class ThirdPartyControllerTest {

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
    void findAll() throws Exception {
    }

    @Test
    void findById() throws Exception {
    }

    @Test
    void create() throws Exception {
    }

    @Test
    void update() throws Exception {
    }

    @Test
    void delete() throws Exception {
    }
}