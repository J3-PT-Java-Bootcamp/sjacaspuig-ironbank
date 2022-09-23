package com.ironhack.ironbank.configuration;

import com.ironhack.ironbank.dto.*;
import com.ironhack.ironbank.model.account.CurrentAccount;
import com.ironhack.ironbank.repository.*;
import com.ironhack.ironbank.service.*;
import lombok.RequiredArgsConstructor;
import net.datafaker.Faker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;


@Configuration
@RequiredArgsConstructor
public class PopulateConfig {

    private final CurrentCheckingAccountService checkingAccountService;
    private final CurrentSavingsAccountService savingsAccountService;
    private final CreditAccountService creditAccountService;
    private final AdminService adminService;
    private final AccountHolderService accountHolderService;
    private final ThirdPartyService thirdPartyService;
    private final TransactionService transactionService;
    private final AccountService accountService;
    private final Faker faker = new Faker();

    @Bean
    public void populate() {

        if (adminService.findAll().size() == 0) {
            // Populate admin
            System.out.println("*******************************");
            System.out.println("Populating admins...");
            System.out.println("*******************************");
            for (int i = 0; i < 3; i++) {
                var adminDTO = new AdminDTO();
                adminDTO.setFirstName(faker.name().firstName());
                adminDTO.setLastName(faker.name().lastName());
                adminDTO.setUsername(faker.internet().domainName());
                adminDTO.setEmail(faker.internet().emailAddress());
                adminDTO.setPassword("password");
                var user = adminService.create(adminDTO);
                System.out.println("Status: " + user.getStatus() + " - " + user.getMessage());
            }
        }

        if (accountHolderService.findAll().size() == 0) {
            // Populate account holders
            System.out.println("*******************************");
            System.out.println("Populating account holders...");
            System.out.println("*******************************");
            for (int i = 0; i < 5; i++) {
                var accountHolderDTO = new AccountHolderDTO();
                accountHolderDTO.setFirstName(faker.name().firstName());
                accountHolderDTO.setLastName(faker.name().lastName());
                accountHolderDTO.setUsername(faker.internet().domainName());
                accountHolderDTO.setEmail(faker.internet().emailAddress());
                accountHolderDTO.setBirthDate(faker.date().birthday(23, 26));

                var addressDTO = new AddressDTO();
                addressDTO.setStreet(faker.address().streetAddress());
                addressDTO.setNumber(faker.address().buildingNumber());
                addressDTO.setCity(faker.address().city());
                addressDTO.setCountry(faker.address().country());
                addressDTO.setPostalCode(faker.address().zipCode());

                accountHolderDTO.setPrimaryAddress(addressDTO);

                if (i % 2 == 0) {
                    var secondaryAddressDTO = new AddressDTO();
                    secondaryAddressDTO.setStreet(faker.address().streetAddress());
                    secondaryAddressDTO.setNumber(faker.address().buildingNumber());
                    secondaryAddressDTO.setCity(faker.address().city());
                    secondaryAddressDTO.setCountry(faker.address().country());
                    secondaryAddressDTO.setPostalCode(faker.address().zipCode());
                    accountHolderDTO.setSecondaryAddress(secondaryAddressDTO);
                }

                accountHolderDTO.setPassword("password");

                var user = accountHolderService.create(accountHolderDTO);
                System.out.println("Status: " + user.getStatus() + " - " + user.getMessage());
            }
        }

        if (thirdPartyService.findAll().size() == 0) {
            // Populate third parties
            System.out.println("*******************************");
            System.out.println("Populating third parties...");
            System.out.println("*******************************");
            for (int i = 0; i < 5; i++) {
                var thirdPartyDTO = new ThirdPartyDTO();
                thirdPartyDTO.setFirstName(faker.name().firstName());
                thirdPartyDTO.setLastName(faker.name().lastName());
                thirdPartyDTO.setHashedKey(faker.hashing().sha256());
                var user = thirdPartyService.create(thirdPartyDTO);
                System.out.println("Status: " + user.getStatus() + " - " + user.getMessage());
            }
        }

        if (checkingAccountService.findAll().size() == 0) {
            // Populate checking accounts
            System.out.println("*******************************");
            System.out.println("Populating checking accounts...");
            System.out.println("*******************************");
            var count = 0;
            while (count < 5) {
                try {
                    var checkingAccountDTO = new CurrentCheckingAccountDTO();

                    // Add account holder 2 if random number is even
                    if (faker.number().randomNumber(1, false) % 2 == 0) {
                        var accountHolder2 = accountHolderService.findAll().get(faker.number().numberBetween(0, accountHolderService.findAll().size() - 1));
                        checkingAccountDTO.setSecondaryOwner(accountHolder2.getId());
                    }
                    var balance = new MoneyDTO();
                    balance.setAmount(new BigDecimal(faker.number().randomNumber()));
                    checkingAccountDTO.setBalance(balance);
                    checkingAccountDTO.setSecretKey(faker.internet().password());

                    var accountHolder = accountHolderService.findAll().get(faker.number().numberBetween(0, accountHolderService.findAll().size() - 1));

                    checkingAccountDTO.setPrimaryOwner(accountHolder.getId());
                    checkingAccountService.create(checkingAccountDTO);

                    count++;
                } catch (Exception e) {
                    // Balance greater than the maximum allowed or negative
                    System.out.println(e.getMessage());
                }
            }
        }

        if (savingsAccountService.findAll().size() == 0) {
            // Populate savings accounts
            System.out.println("*******************************");
            System.out.println("Populating savings accounts...");
            System.out.println("*******************************");
            var countB = 0;
            while (countB < 5) {
                try {
                    var savingsAccountDTO = new CurrentSavingsAccountDTO();

                    // Add account holder 2 if random number is even
                    if (faker.number().randomNumber(1, false) % 2 == 0) {
                        var accountHolder2 = accountHolderService.findAll().get(faker.number().numberBetween(0, accountHolderService.findAll().size() - 1));
                        savingsAccountDTO.setSecondaryOwner(accountHolder2.getId());
                    }

                    // Add minimumBalance if random number is even (Before balance)
                    if (faker.number().randomNumber(1, false) % 2 == 0) {
                        var minimumBalance = new MoneyDTO();
                        minimumBalance.setAmount(new BigDecimal(faker.number().randomNumber()));
                        savingsAccountDTO.setMinimumBalance(minimumBalance);
                    }

                    // Add interestRate if random number is even
                    if (faker.number().randomNumber(1, false) % 2 == 0) {
                        // Random double no equal to 0
                        var interestRate = faker.number().randomDouble(4, 0, 1);
                        while (interestRate == 0) {
                            interestRate = faker.number().randomDouble(4, 0, 1);
                        }
                        savingsAccountDTO.setInterestRate(new BigDecimal(interestRate));
                    }

                    var balance = new MoneyDTO();
                    balance.setAmount(new BigDecimal(faker.number().randomNumber()));
                    savingsAccountDTO.setBalance(balance);
                    savingsAccountDTO.setSecretKey(faker.internet().password());
                    var accountHolder = accountHolderService.findAll().get(faker.number().numberBetween(0, accountHolderService.findAll().size() - 1));
                    savingsAccountDTO.setPrimaryOwner(accountHolder.getId());
                    savingsAccountService.create(savingsAccountDTO);
                    countB++;
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                }
            }
        }

        if (creditAccountService.findAll().size() == 0) {
            // Populate credit accounts
            System.out.println("*******************************");
            System.out.println("Populating credit accounts...");
            System.out.println("*******************************");
            var countC = 0;
            while (countC < 5) {
                try {

                    var creditAccountDTO = new CreditAccountDTO();

                    // Add account holder 2 if random number is even
                    if (faker.number().randomNumber(1, false) % 2 == 0) {
                        var accountHolder2 = accountHolderService.findAll().get(faker.number().numberBetween(0, accountHolderService.findAll().size() - 1));
                        creditAccountDTO.setSecondaryOwner(accountHolder2.getId());
                    }

                    //Add creditLimit if random number is even (Before balance)
                    if (faker.number().randomNumber(1, false) % 2 == 0) {
                        var creditLimit = new MoneyDTO();
                        creditLimit.setAmount(new BigDecimal(faker.number().numberBetween(0, 200000)));
                        creditAccountDTO.setCreditLimit(creditLimit);
                    }

                    //Add interestRate if random number is even
                    if (faker.number().randomNumber(1, false) % 2 == 0) {
                        // Random double no equal to 0
                        var interestRate = faker.number().randomDouble(4, 0, 1);
                        while (interestRate == 0) {
                            interestRate = faker.number().randomDouble(4, 0, 1);
                        }
                        creditAccountDTO.setInterestRate(new BigDecimal(interestRate));
                    }

                    var balance = new MoneyDTO();
                    balance.setAmount(new BigDecimal(faker.number().numberBetween(0, 200)));
                    creditAccountDTO.setBalance(balance);
                    var accountHolder = accountHolderService.findAll().get(faker.number().numberBetween(0, accountHolderService.findAll().size() - 1));
                    creditAccountDTO.setPrimaryOwner(accountHolder.getId());
                    creditAccountService.create(creditAccountDTO);
                    countC++;
                } catch (Exception e) {
                    // Balance greater than the maximum allowed or negative
                    System.out.println(e.getMessage());
                }
            }
        }

        if (transactionService.findAll().size() == 0) {
            // Populate transactions between account holder 1 and 2
            System.out.println("*******************************");
            System.out.println("Populating transactions between account holder 1 and 2...");
            System.out.println("*******************************");
            var countD = 0;
            while (countD < 15) {
                try {
                    var transactionDTO = new TransactionDTO();
                    var account1 = accountService.findAll().get(faker.number().numberBetween(0, accountService.findAll().size() - 1));
                    var account2 = accountService.findAll().get(faker.number().numberBetween(0, accountService.findAll().size() - 1));
                    transactionDTO.setSourceAccount(account1.getIban());
                    transactionDTO.setTargetAccount(account2.getIban());
                    var primaryOwner = accountHolderService.findById(account1.getPrimaryOwner());
                    transactionDTO.setName(primaryOwner.getFirstName() + " " + primaryOwner.getLastName());
                    var amount = new MoneyDTO();
                    amount.setAmount(new BigDecimal(faker.number().randomNumber()));
                    transactionDTO.setAmount(amount);

                    // Add concept if random number is even
                    if (faker.number().randomNumber(1, false) % 2 == 0) {
                        transactionDTO.setConcept(faker.lorem().sentence());
                    }

                    // Add random admin
                    var admin = adminService.findAll().get(faker.number().numberBetween(0, adminService.findAll().size() - 1));
                    transactionDTO.setAdminId(admin.getId());

                    transactionService.create(transactionDTO);
                    countD++;
                } catch (Exception e) {
                    // Balance greater than the maximum allowed or negative
                    System.out.println(e.getMessage());
                }
            }

            // Populate transactions between account holder 1 and third party
            System.out.println("*******************************");
            System.out.println("Populating transactions between account holder 1 and third party...");
            System.out.println("*******************************");
            var countE = 0;
            while (countE < 15) {
                try {
                    var transactionDTO = new TransactionDTO();
                    var account = accountService.findAllAccounts().get(faker.number().numberBetween(0, accountService.findAll().size() - 1));
                    transactionDTO.setSourceAccount(account.getIban());

                    if(account instanceof CurrentAccount) {
                        var studentAccount = (CurrentAccount) account;
                        transactionDTO.setSecretKey(studentAccount.getSecretKey());
                    }

                    var thirdParty = thirdPartyService.findAll().get(faker.number().numberBetween(0, thirdPartyService.findAll().size() - 1));
                    transactionDTO.setHashedKey(thirdParty.getHashedKey());

                    transactionDTO.setName(Faker.instance().name().fullName());

                    var amount = new MoneyDTO();
                    amount.setAmount(new BigDecimal(faker.number().randomNumber()));
                    transactionDTO.setAmount(amount);

                    // Add concept if random number is even
                    if (faker.number().randomNumber(1, false) % 2 == 0) {
                        transactionDTO.setConcept(faker.lorem().sentence());
                    }

                    // Add fee if random number is even
                    if (faker.number().randomNumber(1, false) % 2 == 0) {
                        var fee = new MoneyDTO();
                        fee.setAmount(new BigDecimal(faker.number().numberBetween(1, 10)));
                        transactionDTO.setFee(fee);
                    }

                    // Add random admin
                    var admin = adminService.findAll().get(faker.number().numberBetween(0, adminService.findAll().size() - 1));
                    transactionDTO.setAdminId(admin.getId());

                    transactionService.create(transactionDTO);
                    countE++;
                } catch (Exception e) {
                    // Balance greater than the maximum allowed or negative
                    System.out.println(e.getMessage());
                }
            }

            // Populate transactions between third party and account holder 1
            System.out.println("*******************************");
            System.out.println("Populating transactions between third party and account holder 1...");
            System.out.println("*******************************");
            var countF = 0;
            while (countF < 15) {
                try {
                    var transactionDTO = new TransactionDTO();
                    var account = accountService.findAllAccounts().get(faker.number().numberBetween(0, accountService.findAll().size() - 1));
                    transactionDTO.setTargetAccount(account.getIban());

                    if (faker.number().randomNumber(1, false) % 3 == 0 && faker.number().randomNumber(1, false) % 3 == 0) {
                        transactionDTO.setSecretKey(faker.internet().password());
                    } else {
                        if(account instanceof CurrentAccount) {
                            var studentAccount = (CurrentAccount) account;
                            transactionDTO.setSecretKey(studentAccount.getSecretKey());
                        }
                    }

                    var thirdParty = thirdPartyService.findAll().get(faker.number().numberBetween(0, thirdPartyService.findAll().size() - 1));
                    transactionDTO.setHashedKey(thirdParty.getHashedKey());

                    var primaryOwner = account.getPrimaryOwner();
                    transactionDTO.setName(primaryOwner.getFirstName() + " " + primaryOwner.getLastName());

                    var amount = new MoneyDTO();
                    amount.setAmount(new BigDecimal(faker.number().randomNumber()));
                    transactionDTO.setAmount(amount);

                    // Add concept if random number is even
                    if (faker.number().randomNumber(1, false) % 2 == 0) {
                        transactionDTO.setConcept(faker.lorem().sentence());
                    }

                    // Add fee if random number is even
                    if (faker.number().randomNumber(1, false) % 2 == 0) {
                        var fee = new MoneyDTO();
                        fee.setAmount(new BigDecimal(faker.number().numberBetween(1, 10)));
                        transactionDTO.setFee(fee);
                    }

                    // Add random admin
                    var admin = adminService.findAll().get(faker.number().numberBetween(0, adminService.findAll().size() - 1));
                    transactionDTO.setAdminId(admin.getId());

                    transactionService.create(transactionDTO);
                    countF++;
                } catch (Exception e) {
                    // Balance greater than the maximum allowed or negative
                    System.out.println(e.getMessage());
                }
            }
        }
    }
}
