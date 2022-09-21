package com.ironhack.ironbank.service;

import com.ironhack.ironbank.dto.*;
import com.ironhack.ironbank.dto.response.InterestRateResponse;
import com.ironhack.ironbank.enums.TransactionStatus;
import com.ironhack.ironbank.enums.TransactionType;
import com.ironhack.ironbank.model.Money;
import com.ironhack.ironbank.model.Transaction;
import com.ironhack.ironbank.model.account.*;
import com.ironhack.ironbank.model.user.AccountHolder;
import com.ironhack.ironbank.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
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

    @Override
    public TransactionDTO create(TransactionDTO transactionDTO) {
        Transaction transaction = null;
        try {
            // Initialize transaction setting the status to PENDING
            transaction = initializeTransaction(transactionDTO);

            if (transactionDTO.getHashedKey() != null && transactionDTO.getSourceAccount() == null) { // It's sent by a third party to an account holder

                // Check if the secret key from the third party matches the one in the database
                var account = accountService.findById(transactionDTO.getTargetAccount()).orElseThrow(() -> new IllegalArgumentException("Target account not found"));
                if (transactionDTO.getSecretKey() != null && !transactionDTO.getSecretKey().equals(account.getSecretKey())) {
                    throw new IllegalArgumentException("Secret key does not match");
                }
                transaction.setTargetAccount(account);

                // If it matches, create the transaction
                var amount = Money.fromDTO(transactionDTO.getAmount());
                var amountToBeAdded = account.getBalance().increaseAmount(amount);
                account.setBalance(new Money(amountToBeAdded));
                var accountDTO = AccountDTO.fromEntity(account);
                updateAccount(accountDTO.getIban(), accountDTO);

                var targetAccountDTO = accountService.findByIban(transactionDTO.getTargetAccount());
                var primaryOwnerDTO = accountHolderService.findById(targetAccountDTO.getPrimaryOwner());
                var primaryOwner = AccountHolder.fromDTO(primaryOwnerDTO);
                AccountHolder secondaryOwner = null;
                if(targetAccountDTO.getSecondaryOwner() != null) {
                    var secondaryOwnerDTO = accountHolderService.findById(targetAccountDTO.getSecondaryOwner());
                    secondaryOwner = AccountHolder.fromDTO(secondaryOwnerDTO);
                }
                var targetAccount = Account.fromDTO(targetAccountDTO, primaryOwner, secondaryOwner);

                var transactionUpdated = Transaction.fromDTO(transactionDTO, null, targetAccount);
                transactionUpdated.setId(transaction.getId());
                transactionUpdated.setStatus(TransactionStatus.COMPLETED);
                transactionUpdated = transactionRepository.save(transactionUpdated);

                return TransactionDTO.fromEntity(transactionUpdated);

            } else if (transactionDTO.getHashedKey() != null && transactionDTO.getSourceAccount() != null) { // It's sent by an account holder to a third party

                // Secret key is not needed in this case
                var account = accountService.findById(transactionDTO.getSourceAccount()).orElseThrow(() -> new IllegalArgumentException("Sender account not found"));
                transaction.setSourceAccount(account);

                // Check if the account has sufficient funds
                if (account.getBalance().getAmount().compareTo(transactionDTO.getAmount().getAmount()) < 0) {
                    throw new RuntimeException("Insufficient funds in the sender's account");
                } else {
                    var amount = Money.fromDTO(transactionDTO.getAmount());
                    var fee = new Money(new BigDecimal(0));
                    if (transactionDTO.getFee() != null) {
                        fee = Money.fromDTO(transactionDTO.getFee());
                    }
                    var amountToBeSubtracted = account.getBalance().decreaseAmount(amount.increaseAmount(fee));
                    account.setBalance(new Money(amountToBeSubtracted));
                    var accountDTOUpdated = AccountDTO.fromEntity(account);
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
                var sourceAccount = Account.fromDTO(sourceAccountDTO, primaryOwner, secondaryOwner);

                var transactionUpdated = Transaction.fromDTO(transactionDTO, sourceAccount, null);
                transactionUpdated.setId(transaction.getId());
                transactionUpdated.setStatus(TransactionStatus.COMPLETED);
                transactionUpdated = transactionRepository.save(transactionUpdated);

                return TransactionDTO.fromEntity(transactionUpdated);

            } else { // it's sent by an account holder to another account holder

                // Secret key is not needed in this case
                var senderAccount = accountService.findById(transactionDTO.getSourceAccount()).orElseThrow(() -> new IllegalArgumentException("Sender account not found"));
                transaction.setSourceAccount(senderAccount);
                var receiverAccount = accountService.findById(transactionDTO.getTargetAccount()).orElseThrow(() -> new IllegalArgumentException("Target account not found"));
                transaction.setTargetAccount(receiverAccount);

                // Check if the accounts are the same
                if (transactionDTO.getSourceAccount().equals(transactionDTO.getTargetAccount())) {
                    throw new RuntimeException("You can't send money to the same account");
                }

                // Check if the sender account has sufficient funds
                if (senderAccount.getBalance().getAmount().compareTo(transactionDTO.getAmount().getAmount()) < 0) {
                    throw new RuntimeException("Insufficient funds in the sender's account");
                } else {
                    var amount = Money.fromDTO(transactionDTO.getAmount());
                    var fee = new Money(new BigDecimal(0));
                    if (transactionDTO.getFee() != null) {
                        fee = Money.fromDTO(transactionDTO.getFee());
                    }
                    var amountToBeSubtracted = senderAccount.getBalance().decreaseAmount(amount.increaseAmount(fee));
                    senderAccount.setBalance(new Money(amountToBeSubtracted));
                    var senderAccountDTOUpdated = AccountDTO.fromEntity(senderAccount);
                    updateAccount(senderAccountDTOUpdated.getIban(), senderAccountDTOUpdated);
                }

                // Add the amount to the receiver account
                var amount = Money.fromDTO(transactionDTO.getAmount());
                var amountToBeAdded = receiverAccount.getBalance().increaseAmount(amount);
                receiverAccount.setBalance(new Money(amountToBeAdded));
                var receiverAccountDTOUpdated = AccountDTO.fromEntity(receiverAccount);
                updateAccount(receiverAccountDTOUpdated.getIban(), receiverAccountDTOUpdated);

                // Create the transaction
                var transactionUpdated = Transaction.fromDTO(transactionDTO, senderAccount, receiverAccount);
                transactionUpdated.setId(transaction.getId());
                transactionUpdated.setStatus(TransactionStatus.COMPLETED);
                transactionUpdated = transactionRepository.save(transactionUpdated);

                return TransactionDTO.fromEntity(transactionUpdated);
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
        var transaction = Transaction.fromDTO(transactionDTO, null, null);
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
}
