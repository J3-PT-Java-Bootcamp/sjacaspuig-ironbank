package com.ironhack.ironbank.utils;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class IbanGeneratorTest {

    @Autowired
    IbanGenerator ibanGenerator;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void generateIban() {
        // Create an IBAN and check it starts with ES
        String iban = ibanGenerator.generateIban();
        assertTrue(iban.startsWith("ES"));
    }
}