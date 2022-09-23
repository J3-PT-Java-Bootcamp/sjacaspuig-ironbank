package com.ironhack.ironbank.service;

import com.ironhack.ironbank.dto.*;
import com.ironhack.ironbank.dto.response.InterestRateResponse;
import com.ironhack.ironbank.enums.TransactionStatus;
import com.ironhack.ironbank.enums.TransactionType;
import com.ironhack.ironbank.model.Money;
import com.ironhack.ironbank.model.Transaction;
import com.ironhack.ironbank.model.account.*;
import com.ironhack.ironbank.model.user.AccountHolder;
import com.ironhack.ironbank.model.user.Admin;
import com.ironhack.ironbank.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountService accountService;
    private final AccountHolderService accountHolderService;
    private final CreditAccountService creditAccountService;
    private final CurrentCheckingAccountService currentCheckingAccountService;
    private final CurrentSavingsAccountService currentSavingsAccountService;
    private final CurrentStudentCheckingAccountService currentStudentCheckingAccountService;
    private final AdminService adminService;

    @Override
    public TransactionDTO create(TransactionDTO transactionDTO) {
        Transaction transaction = null;
        try {
            // Initialize transaction setting the status to PENDING
            transaction = initializeTransaction(transactionDTO);

            if (transactionDTO.getHashedKey() == null && transactionDTO.getType().equals(TransactionType.ADMIN_MOVEMENT)) { // It's a modification of balance by and admin

                if (transactionDTO.getSourceAccount() != null && transactionDTO.getTargetAccount() == null) { // When the admin decreases the balance of an account

                    var sourceAccount = accountService.findById(transactionDTO.getSourceAccount()).orElseThrow(() -> new IllegalArgumentException("Sender account with IBAN " + transactionDTO.getSourceAccount() + " does not exist"));
                    transaction.setSourceAccount(sourceAccount);

                    return createTransactionDecreasingSourceBalanceWithoutTargetAccount(transactionDTO, transaction, sourceAccount);
                } else if (transactionDTO.getSourceAccount() == null && transactionDTO.getTargetAccount() != null) { // When the admin increases the balance of an account

                    var targetAccount = accountService.findById(transactionDTO.getTargetAccount()).orElseThrow(() -> new IllegalArgumentException("Target account with IBAN " + transactionDTO.getTargetAccount() + " does not exist"));
                    transaction.setTargetAccount(targetAccount);

                    return createTransactionIncreasingTargetBalanceWithoutSourceAccount(transactionDTO, transaction, targetAccount);
                } else {
                    return saveFailedTransaction(transaction, "An admin movement not allow to have source and target account. Transaction not created");
                }

            } else if (transactionDTO.getHashedKey() != null && !transactionDTO.getType().equals(TransactionType.ADMIN_MOVEMENT)) { // It's between a third party and an owner account

                if (transactionDTO.getSourceAccount() == null && transactionDTO.getTargetAccount() != null) { // Third party to owner account

                    // Check if the secret key from the third party matches the one in the database
                    var targetAccount = accountService.findById(transactionDTO.getTargetAccount()).orElseThrow(() -> new IllegalArgumentException("Target account with IBAN " + transactionDTO.getTargetAccount() + " does not exist"));
                    if (transactionDTO.getSecretKey() != null && !transactionDTO.getSecretKey().equals(targetAccount.getSecretKey())) {
                        throw new IllegalArgumentException("Secret key does not match");
                    }
                    transaction.setTargetAccount(targetAccount);

                    return createTransactionIncreasingTargetBalanceWithoutSourceAccount(transactionDTO, transaction, targetAccount);

                } else if (transactionDTO.getSourceAccount() != null && transactionDTO.getTargetAccount() == null) { // Owner account to third party

                    var sourceAccount = accountService.findById(transactionDTO.getSourceAccount()).orElseThrow(() -> new IllegalArgumentException("Sender account with IBAN " + transactionDTO.getSourceAccount() + " does not exist"));
                    transaction.setSourceAccount(sourceAccount);

                    return createTransactionDecreasingSourceBalanceWithoutTargetAccount(transactionDTO, transaction, sourceAccount);
                } else {
                    return saveFailedTransaction(transaction, "A third party transaction not allow to have source and target account. Transaction not created");
                }

            } else if (transactionDTO.getHashedKey() == null && !transactionDTO.getType().equals(TransactionType.ADMIN_MOVEMENT) && transactionDTO.getSourceAccount() != null && transactionDTO.getTargetAccount() != null) { // it's sent by an account holder to another account holder

                // Secret key is not needed in this case
                var sourceAccount = accountService.findById(transactionDTO.getSourceAccount()).orElseThrow(() -> new IllegalArgumentException("Sender account not found"));
                transaction.setSourceAccount(sourceAccount);
                var targetAccount = accountService.findById(transactionDTO.getTargetAccount()).orElseThrow(() -> new IllegalArgumentException("Target account not found"));
                transaction.setTargetAccount(targetAccount);

                return createTransactionBetweenOurAccountHolders(transactionDTO, transaction, sourceAccount, targetAccount);

            } else {
                return saveFailedTransaction(transaction, "The transaction is not valid. Transaction not created");
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return saveFailedTransaction(transaction, e.getMessage());
        }
    }

    @Override
    public TransactionDTO findById(Long id) {
        var transaction = transactionRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Third party not found"));
        return TransactionDTO.fromEntity(transaction);
    }

    @Override
    public List<TransactionDTO> findAll() {
        var transactions = transactionRepository.findAll();
        return TransactionDTO.fromEntities(transactions);
    }

    private Transaction initializeTransaction(TransactionDTO transactionDTO) {
        var adminDTO = adminService.findById(transactionDTO.getAdminId());
        var admin = Admin.fromDTO(adminDTO);
        var transaction = Transaction.fromDTO(transactionDTO, null, null, admin);
        transaction.setStatus(TransactionStatus.PENDING);
        return transactionRepository.save(transaction);
    }

    private TransactionDTO saveFailedTransaction(Transaction transaction, String message) {
        if (transaction != null) {
            transaction.setFailureReason(message);
            transaction.setStatus(TransactionStatus.FAILED);
            transaction = transactionRepository.save(transaction);
            return TransactionDTO.fromEntity(transaction);
        } else {
            throw new RuntimeException("Transaction could not be saved");
        }
    }

    @Override
    public void createInterestTransaction(Account account, InterestRateResponse response) {
        var transaction = new Transaction();
        transaction.setTargetAccount(account);
        transaction.setAmount(response.getEarnings());
        transaction.setName(account.getPrimaryOwner().getFirstName() + " " + account.getPrimaryOwner().getLastName());
        transaction.setConcept("For " + response.getTimesApplied() + " periods an interest of " + response.getInterestRateApplied().multiply(new BigDecimal("100")) + "% has been applied.");
        transaction.setStatus(TransactionStatus.COMPLETED);
        transaction.setType(TransactionType.INTEREST);
        transactionRepository.save(transaction);
    }

    @Override
    public void createPenaltyMinBalanceTransaction(Account account) {
        var transaction = new Transaction();
        transaction.setSourceAccount(account);
        transaction.setAmount(Account.PENALTY_FEE);
        transaction.setName(account.getPrimaryOwner().getFirstName() + " " + account.getPrimaryOwner().getLastName());
        transaction.setConcept("Penalty fee for not having the minimum balance");
        transaction.setStatus(TransactionStatus.COMPLETED);
        transaction.setType(TransactionType.PENALTY_MIN_BALANCE);
        transactionRepository.save(transaction);
    }

    @Override
    public List<TransactionDTO> findByIban(String iban) {
        var transactions = transactionRepository.findBySourceAccountIbanOrTargetAccountIban(iban, iban);
        return TransactionDTO.fromEntities(transactions);
    }

    @Override
    public List<TransactionDTO> findByAccountHolderId(String id) {
        var transactions = transactionRepository.findBySourceAccountPrimaryOwnerIdOrSourceAccountSecondaryOwnerIdOrTargetAccountPrimaryOwnerIdOrTargetAccountSecondaryOwnerId(id, id, id, id);
        return TransactionDTO.fromEntities(transactions);
    }
    
    private AccountDTO updateAccount(String iban, AccountDTO accountDTO) {
        var accountUpdated = accountService.findById(iban).orElseThrow(() -> new IllegalArgumentException("Account not found"));
        accountUpdated.setBalance(Money.fromDTO(accountDTO.getBalance()));

        // Check account type, cast and update
        update(accountUpdated);

        return AccountDTO.fromEntity(accountUpdated);
    }

    private void update(Account account) {
        if (account instanceof CreditAccount) {
            var creditAccountUpdated = (CreditAccount) account;
            var dto = CreditAccountDTO.fromEntity(creditAccountUpdated);
            creditAccountService.update(account.getIban(), dto);
        } else if (account instanceof CurrentCheckingAccount) {
            var currentCheckingAccountUpdated = (CurrentCheckingAccount) account;
            var dto = CurrentCheckingAccountDTO.fromEntity(currentCheckingAccountUpdated);
            currentCheckingAccountService.update(account.getIban(), dto);
        } else if (account instanceof CurrentSavingsAccount) {
            var currentSavingsAccountUpdated = (CurrentSavingsAccount) account;
            var dto = CurrentSavingsAccountDTO.fromEntity(currentSavingsAccountUpdated);
            currentSavingsAccountService.update(account.getIban(), dto);
        } else if (account instanceof CurrentStudentCheckingAccount) {
            var currentStudentCheckingAccountUpdated = (CurrentStudentCheckingAccount) account;
            var dto = CurrentStudentCheckingAccountDTO.fromEntity(currentStudentCheckingAccountUpdated);
            currentStudentCheckingAccountService.update(account.getIban(), dto);
        }
    }

    private TransactionDTO createTransactionDecreasingSourceBalanceWithoutTargetAccount(TransactionDTO transactionDTO, Transaction currentTransaction, Account sourceAccount){
        // Check if the account has sufficient funds
        if (sourceAccount.getBalance().getAmount().compareTo(transactionDTO.getAmount().getAmount()) < 0) {
            throw new RuntimeException("Insufficient funds in the sender's account");
        } else {
            var amount = Money.fromDTO(transactionDTO.getAmount());
            var fee = new Money(new BigDecimal(0));
            if (transactionDTO.getFee() != null) {
                fee = Money.fromDTO(transactionDTO.getFee());
            }
            var amountToBeSubtracted = sourceAccount.getBalance().decreaseAmount(amount.increaseAmount(fee));
            sourceAccount.setBalance(new Money(amountToBeSubtracted));
            var accountDTOUpdated = AccountDTO.fromEntity(sourceAccount);
            updateAccount(accountDTOUpdated.getIban(), accountDTOUpdated);
        }

        var sourceAccountDTO = accountService.findByIban(transactionDTO.getSourceAccount());
        var primaryOwnerDTO = accountHolderService.findById(sourceAccountDTO.getPrimaryOwner());
        var primaryOwner = AccountHolder.fromDTO(primaryOwnerDTO);
        AccountHolder secondaryOwner = null;
        if(sourceAccountDTO.getSecondaryOwner() != null) {
            var secondaryOwnerDTO = accountHolderService.findById(sourceAccountDTO.getSecondaryOwner());
            secondaryOwner = AccountHolder.fromDTO(secondaryOwnerDTO);
        }
        var sourceAccountUpdated = Account.fromDTO(sourceAccountDTO, primaryOwner, secondaryOwner);

        var adminDTO = adminService.findById(transactionDTO.getAdminId());
        var admin = Admin.fromDTO(adminDTO);
        var transactionUpdated = Transaction.fromDTO(transactionDTO, sourceAccountUpdated, null, admin);
        transactionUpdated.setId(currentTransaction.getId());
        transactionUpdated.setStatus(TransactionStatus.COMPLETED);
        transactionUpdated = transactionRepository.save(transactionUpdated);

        return TransactionDTO.fromEntity(transactionUpdated);
    }

    private TransactionDTO createTransactionIncreasingTargetBalanceWithoutSourceAccount(TransactionDTO transactionDTO, Transaction currentTransaction, Account targetAccount){

        // If it matches, create the transaction
        var amount = Money.fromDTO(transactionDTO.getAmount());
        var amountToBeAdded = targetAccount.getBalance().increaseAmount(amount);
        targetAccount.setBalance(new Money(amountToBeAdded));
        var accountDTO = AccountDTO.fromEntity(targetAccount);
        updateAccount(accountDTO.getIban(), accountDTO);

        var targetAccountDTO = accountService.findByIban(transactionDTO.getTargetAccount());
        var primaryOwnerDTO = accountHolderService.findById(targetAccountDTO.getPrimaryOwner());
        var primaryOwner = AccountHolder.fromDTO(primaryOwnerDTO);
        AccountHolder secondaryOwner = null;
        if(targetAccountDTO.getSecondaryOwner() != null) {
            var secondaryOwnerDTO = accountHolderService.findById(targetAccountDTO.getSecondaryOwner());
            secondaryOwner = AccountHolder.fromDTO(secondaryOwnerDTO);
        }
        var targetAccountUpdated = Account.fromDTO(targetAccountDTO, primaryOwner, secondaryOwner);

        var adminDTO = adminService.findById(transactionDTO.getAdminId());
        var admin = Admin.fromDTO(adminDTO);
        var transactionUpdated = Transaction.fromDTO(transactionDTO, null, targetAccountUpdated, admin);
        transactionUpdated.setId(currentTransaction.getId());
        transactionUpdated.setStatus(TransactionStatus.COMPLETED);
        transactionUpdated = transactionRepository.save(transactionUpdated);

        return TransactionDTO.fromEntity(transactionUpdated);
    }

    private TransactionDTO createTransactionBetweenOurAccountHolders(TransactionDTO transactionDTO, Transaction currentTransaction, Account sourceAccount, Account targetAccount){

        // Check if the accounts are the same
        if (transactionDTO.getSourceAccount().equals(transactionDTO.getTargetAccount())) {
            throw new RuntimeException("You can't send money to the same account");
        }

        // Check if the sender account has sufficient funds
        if (sourceAccount.getBalance().getAmount().compareTo(transactionDTO.getAmount().getAmount()) < 0) {
            throw new RuntimeException("Insufficient funds in the sender's account");
        } else {
            var amount = Money.fromDTO(transactionDTO.getAmount());
            var fee = new Money(new BigDecimal(0));
            if (transactionDTO.getFee() != null) {
                fee = Money.fromDTO(transactionDTO.getFee());
            }
            var amountToBeSubtracted = sourceAccount.getBalance().decreaseAmount(amount.increaseAmount(fee));
            sourceAccount.setBalance(new Money(amountToBeSubtracted));
            var sourceAccountDTOUpdated = AccountDTO.fromEntity(sourceAccount);
            updateAccount(sourceAccountDTOUpdated.getIban(), sourceAccountDTOUpdated);
        }

        // Add the amount to the receiver account
        var amount = Money.fromDTO(transactionDTO.getAmount());
        var amountToBeAdded = targetAccount.getBalance().increaseAmount(amount);
        targetAccount.setBalance(new Money(amountToBeAdded));
        var targetAccountDTOUpdated = AccountDTO.fromEntity(targetAccount);
        updateAccount(targetAccountDTOUpdated.getIban(), targetAccountDTOUpdated);

        // Create the transaction
        var adminDTO = adminService.findById(transactionDTO.getAdminId());
        var admin = Admin.fromDTO(adminDTO);
        var transactionUpdated = Transaction.fromDTO(transactionDTO, sourceAccount, targetAccount, admin);
        transactionUpdated.setId(currentTransaction.getId());
        transactionUpdated.setStatus(TransactionStatus.COMPLETED);
        transactionUpdated = transactionRepository.save(transactionUpdated);

        return TransactionDTO.fromEntity(transactionUpdated);
    }
}
