package com.ironhack.ironbank.utils;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class DateServiceTest {

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getDiffYears() {
        // Create an instant date and reduce it by 2 years
        Instant instant = Instant.now().minusSeconds(60 * 60 * 24 * 365 * 2);
        // Get the difference in years
        int diffYears = DateService.getDiffYears(instant);
        // Check that the difference is 2
        assertEquals(2, diffYears);
    }

    @Test
    void getDiffMonths() {
        // Create an instant date and reduce it by 2 months
        Instant instant = Instant.now().minusSeconds(60 * 60 * 24 * 30 * 2);
        // Get the difference in months
        int diffMonths = DateService.getDiffMonths(instant);
        // Check that the difference is 2
        assertEquals(2, diffMonths);
    }

    @Test
    void parseInstant() {
        // Create an Instant date and parse to Date after that check it has a Date format
        Instant instant = Instant.now();
        DateService.parseInstant(instant);
        assertTrue(DateService.parseInstant(instant) instanceof Date);
    }

    @Test
    void parseDate() {
        // Create a Date and parse to Instant after that check it has a Instant format
        Date date = new Date();
        DateService.parseDate(date);
        assertTrue(DateService.parseDate(date) instanceof Instant);
    }
}