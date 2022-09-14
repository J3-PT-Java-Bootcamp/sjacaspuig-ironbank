package com.ironhack.ironbank.utils;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.Random;

@Configuration
@Getter
public class IbanGenerator {

    @Value("${iban.country.code}")
    private String countryCode = "ES";

    @Value("${iban.checksum}")
    private String checksum = "28";

    @Value("${iban.bank.code}")
    private String bankCode = "1043";

    @Value("${iban.branch.code}")
    private String branchCode = "0001";

    @Value("${iban.bank.checksum}")
    private String bankChecksum = "10";

    public IbanGenerator() {
    }

    public String generateIban() {
        // Random account number between 1000000000 and 9999999999
        Random random = new Random();
        int accountNumber = random.nextInt(900000000) + 100000000;
        return getCountryCode() + getChecksum() + getBankCode() + getBranchCode() + getBankChecksum() + accountNumber;
    }
}
