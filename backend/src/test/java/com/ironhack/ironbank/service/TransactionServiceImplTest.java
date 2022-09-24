package com.ironhack.ironbank.service;

import com.ironhack.ironbank.dto.MoneyDTO;
import com.ironhack.ironbank.dto.TransactionDTO;
import com.ironhack.ironbank.model.account.Account;
import com.ironhack.ironbank.model.account.CurrentAccount;
import com.ironhack.ironbank.repository.TransactionRepository;
import net.datafaker.Faker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TransactionServiceImplTest {

    @Autowired
    TransactionService transactionService;
    @Autowired
    AccountService accountService;
    @Autowired
    AccountHolderService accountHolderService;
    @Autowired
    AdminService adminService;
    @Autowired
    ThirdPartyService thirdPartyService;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void findAll() throws Exception {
        var repositorySize = transactionService.findAll().size();
        assertTrue(repositorySize > 0);
    }

    @Test
    void findById() throws Exception {
        var repositoryTransaction = transactionService.findAll().get(0);
        var transaction = transactionService.findById(repositoryTransaction.getId());
        assertEquals(repositoryTransaction.getId(), transaction.getId());
    }

    @Test
    void create_between_account_holders() throws Exception {
        Faker faker = new Faker();

        var transactionDTO = new TransactionDTO();
        var account1 = accountService.findAll().get(faker.number().numberBetween(0, accountService.findAll().size() - 1));
        var account2 = accountService.findAll().get(faker.number().numberBetween(0, accountService.findAll().size() - 1));
        transactionDTO.setSourceAccount(account1.getIban());
        transactionDTO.setTargetAccount(account2.getIban());
        var primaryOwner = accountHolderService.findById(account1.getPrimaryOwner());
        transactionDTO.setName(primaryOwner.getFirstName() + " " + primaryOwner.getLastName());
        var amount = new MoneyDTO();
        amount.setAmount(new BigDecimal(0));
        transactionDTO.setAmount(amount);
        // Add random admin
        var admin = adminService.findAll().get(faker.number().numberBetween(0, adminService.findAll().size() - 1));
        transactionDTO.setAdminId(admin.getId());
        var transaction = transactionService.create(transactionDTO);
        assertEquals(transactionDTO.getSourceAccount(), transaction.getSourceAccount());
    }

    @Test
    void create_between_account_holder_and_third_party() throws Exception {
        Faker faker = new Faker();

        var transactionDTO = new TransactionDTO();
        var account = accountService.findAllAccounts().get(faker.number().numberBetween(0, accountService.findAll().size() - 1));
        transactionDTO.setSourceAccount(account.getIban());
        transactionDTO.setSecretKey(account.getSecretKey());

        var thirdParty = thirdPartyService.findAll().get(faker.number().numberBetween(0, thirdPartyService.findAll().size() - 1));
        transactionDTO.setHashedKey(thirdParty.getHashedKey());
        transactionDTO.setName(Faker.instance().name().fullName());

        var amount = new MoneyDTO();
        amount.setAmount(new BigDecimal(0));
        transactionDTO.setAmount(amount);

        transactionDTO.setConcept(faker.lorem().sentence());

        var fee = new MoneyDTO();
        fee.setAmount(new BigDecimal(0));
        transactionDTO.setFee(fee);

        // Add random admin
        var admin = adminService.findAll().get(faker.number().numberBetween(0, adminService.findAll().size() - 1));
        transactionDTO.setAdminId(admin.getId());
        var transaction = transactionService.create(transactionDTO);
        assertEquals(transactionDTO.getSourceAccount(), transaction.getSourceAccount());
    }

    @Test
    void create_between_third_party_and_account_holder() throws Exception {
        Faker faker = new Faker();

        var transactionDTO = new TransactionDTO();
        Account account;

        while (true) {
            account = accountService.findAllAccounts().get(faker.number().numberBetween(0, accountService.findAll().size() - 1));
            if (account instanceof CurrentAccount) {
                break;
            }
        }

        transactionDTO.setTargetAccount(account.getIban());
        transactionDTO.setSecretKey(account.getSecretKey());

        var thirdParty = thirdPartyService.findAll().get(faker.number().numberBetween(0, thirdPartyService.findAll().size() - 1));
        transactionDTO.setHashedKey(thirdParty.getHashedKey());

        var primaryOwner = account.getPrimaryOwner();
        transactionDTO.setName(primaryOwner.getFirstName() + " " + primaryOwner.getLastName());

        var amount = new MoneyDTO();
        amount.setAmount(new BigDecimal(0));
        transactionDTO.setAmount(amount);

        var fee = new MoneyDTO();
        fee.setAmount(new BigDecimal(0));
        transactionDTO.setFee(fee);


        var admin = adminService.findAll().get(faker.number().numberBetween(0, adminService.findAll().size() - 1));
        transactionDTO.setAdminId(admin.getId());

        var transaction = transactionService.create(transactionDTO);
        assertEquals(transactionDTO.getSourceAccount(), transaction.getSourceAccount());
    }

    @Test
    void findByIban() throws Exception {

        var repositoryTransaction = transactionService.findAll().get(0);
        var iban = repositoryTransaction.getSourceAccount() != null ? repositoryTransaction.getSourceAccount() : repositoryTransaction.getTargetAccount();
        var transactions = transactionService.findByIban(iban);
        assertTrue(transactions.size() > 0);

    }

    @Test
    void findByAccountHolderId() throws Exception {
        var repositoryTransaction = transactionService.findAll().get(0);
        var iban = repositoryTransaction.getTargetAccount() != null ? repositoryTransaction.getTargetAccount() : repositoryTransaction.getSourceAccount();
        var account = accountService.findByIban(iban);
        var accountHolder = accountHolderService.findById(account.getPrimaryOwner());
        var transactions = transactionService.findByAccountHolderId(accountHolder.getId());
        assertTrue(transactions.size() > 0);
    }
}