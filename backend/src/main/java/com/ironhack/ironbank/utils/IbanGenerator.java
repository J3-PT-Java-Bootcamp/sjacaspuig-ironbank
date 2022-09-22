package com.ironhack.ironbank.utils;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Random;

@ConfigurationProperties(prefix = "iban")
@Getter
@Setter
public class IbanGenerator {

    private String countryCode = "ES";
    private String checksum = "28";
    private String bankCode = "1043";
    private String branchCode = "0001";
    private String bankChecksum = "10";

    public String generateIban() {
        // Random account number between 1000000000 and 9999999999
        Random random = new Random();
        int accountNumber = random.nextInt(900000000) + 100000000;
        return countryCode + checksum + bankCode + branchCode + bankChecksum + accountNumber;
    }
}
