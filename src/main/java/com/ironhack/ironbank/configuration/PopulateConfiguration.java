package com.ironhack.ironbank.configuration;

import com.ironhack.ironbank.dto.*;
import com.ironhack.ironbank.model.account.Account;
import com.ironhack.ironbank.model.account.CurrentAccount;
import com.ironhack.ironbank.repository.*;
import com.ironhack.ironbank.service.*;
import com.ironhack.ironbank.utils.DateService;
import lombok.RequiredArgsConstructor;
import net.datafaker.Faker;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;


@Configuration
@RequiredArgsConstructor
public class PopulateConfiguration {

    private final CurrentCheckingAccountService checkingAccountService;
    private final CurrentStudentCheckingAccountService studentAccountService;
    private final CurrentSavingsAccountService savingsAccountService;
    private final CreditAccountService creditAccountService;
    private final AdminService adminService;
    private final AccountHolderService accountHolderService;
    private final ThirdPartyService thirdPartyService;
    private final CurrentCheckingAccountRepository checkingAccountRepository;
    private final CurrentSavingsAccountRepository savingsAccountRepository;
    private final CurrentStudentCheckingAccountRepository studentAccountRepository;
    private final CreditAccountRepository creditAccountRepository;
    private final TransactionService transactionService;
    private final AccountService accountService;
    private final TransactionRepository transactionRepository;
    private final Faker faker = new Faker();

    @Bean
    public void populate() {

        checkingAccountRepository.deleteAll();
        savingsAccountRepository.deleteAll();
        studentAccountRepository.deleteAll();
        creditAccountRepository.deleteAll();
        transactionRepository.deleteAll();

        if (adminService.findAll().size() == 0) {
            // Populate admin
            for (int i = 0; i < 3; i++) {
                var adminDTO = new AdminDTO();
                adminDTO.setId(faker.internet().uuid());
                adminDTO.setFirstName(faker.name().firstName());
                adminDTO.setLastName(faker.name().lastName());
                adminDTO.setUsername(faker.internet().domainName());
                adminDTO.setEmail(faker.internet().emailAddress());
                adminService.create(adminDTO, "password");
            }
        }

        if (accountHolderService.findAll().size() == 0) {
            // Populate account holders
            for (int i = 0; i < 20; i++) {
                var accountHolderDTO = new AccountHolderDTO();
                accountHolderDTO.setId(faker.internet().uuid());
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

                addressDTO.setStreet(faker.address().streetAddress());
                addressDTO.setNumber(faker.address().buildingNumber());
                accountHolderDTO.setSecondaryAddress(addressDTO);

                accountHolderService.create(accountHolderDTO, "password");
            }
        }


        if (thirdPartyService.findAll().size() == 0) {
            // Populate third parties
            for (int i = 0; i < 7; i++) {
                var thirdPartyDTO = new ThirdPartyDTO();
                thirdPartyDTO.setId(faker.internet().uuid());
                thirdPartyDTO.setFirstName(faker.name().firstName());
                thirdPartyDTO.setLastName(faker.name().lastName());
                thirdPartyDTO.setHashedKey(faker.internet().password());
                thirdPartyService.create(thirdPartyDTO);
            }
        }

        if (checkingAccountService.findAll().size() == 0) {
            // Populate checking accounts
            System.out.println("*******************************");
            System.out.println("Populating checking accounts...");
            System.out.println("*******************************");
            var count = 0;
            while (count < 10) {
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

                    try {
                        checkingAccountDTO.setPrimaryOwner(accountHolder.getId());
                        checkingAccountService.create(checkingAccountDTO);
                    } catch (Exception e) {
                        var studentAccountDTO = CurrentStudentCheckingAccountDTO.fromCurrentAccountDTO(checkingAccountDTO);
                        studentAccountDTO.setPrimaryOwner(accountHolder.getId());
                        studentAccountService.create(studentAccountDTO);
                    }
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
            while (countB < 10) {
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
                        savingsAccountDTO.setInterestRate(new BigDecimal(faker.number().randomDouble(2, 0, 1)));
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
            while (countC < 10) {
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
                        creditAccountDTO.setInterestRate(new BigDecimal(faker.number().randomDouble(2, 0, 1)));
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
            while (countD < 20) {
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
            while (countE < 20) {
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
            while (countF < 20) {
                try {
                    var transactionDTO = new TransactionDTO();
                    var account = accountService.findAllAccounts().get(faker.number().numberBetween(0, accountService.findAll().size() - 1));
                    transactionDTO.setTargetAccount(account.getIban());

                    if (faker.number().randomNumber(1, false) % 2 == 0) {
                        if(account instanceof CurrentAccount) {
                            var studentAccount = (CurrentAccount) account;
                            transactionDTO.setSecretKey(studentAccount.getSecretKey());
                        }
                    } else {
                        transactionDTO.setSecretKey(faker.internet().password());
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
