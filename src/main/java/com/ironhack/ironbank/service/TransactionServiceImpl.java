package com.ironhack.ironbank.service;

import com.ironhack.ironbank.dto.*;
import com.ironhack.ironbank.enums.AccountType;
import com.ironhack.ironbank.enums.TransactionStatus;
import com.ironhack.ironbank.model.Money;
import com.ironhack.ironbank.model.Transaction;
import com.ironhack.ironbank.model.account.*;
import com.ironhack.ironbank.model.user.AccountHolder;
import com.ironhack.ironbank.repository.AccountRepository;
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

    @Override
    public TransactionDTO create(TransactionDTO transactionDTO) {
        Transaction transaction = null;
        try {
            transaction = initializeTransaction(transactionDTO);

            if (transactionDTO.getHashedKey() != null && transactionDTO.getSourceAccount() == null) { // It's sent by a third party to an account holder

                // Check if the secret key from the third party matches the one in the database
                var account = accountService.findById(transactionDTO.getTargetAccount()).orElseThrow(() -> new IllegalArgumentException("Target account not found"));
                transaction.setTargetAccount(account);

                // If it matches, create the transaction
                var amount = Money.fromDTO(transactionDTO.getAmount());
                var amountToBeAdded = account.getBalance().increaseAmount(amount);
                account.setBalance(new Money(amountToBeAdded));
                var accountDTO = AccountDTO.fromEntity(account);
                accountService.update(accountDTO.getIban(), accountDTO);

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
                    accountService.update(accountDTOUpdated.getIban(), accountDTOUpdated);
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
                    accountService.update(senderAccountDTOUpdated.getIban(), senderAccountDTOUpdated);
                }

                // Add the amount to the receiver account
                var amount = Money.fromDTO(transactionDTO.getAmount());
                var amountToBeAdded = receiverAccount.getBalance().increaseAmount(amount);
                receiverAccount.setBalance(new Money(amountToBeAdded));
                var receiverAccountDTOUpdated = AccountDTO.fromEntity(receiverAccount);
                accountService.update(receiverAccountDTOUpdated.getIban(), receiverAccountDTOUpdated);

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
        transaction.setFailureReason(message);
        transaction.setStatus(TransactionStatus.FAILED);
        transaction = transactionRepository.save(transaction);
        return TransactionDTO.fromEntity(transaction);
    }
}
