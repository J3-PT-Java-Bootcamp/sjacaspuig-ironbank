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


        if (transactionDTO.getHashedKey() != null && transactionDTO.getSourceAccount() == null) { // It's sent by a third party to an account holder

            // Check if the secret key from the third party matches the one in the database
            var account = accountService.findById(transactionDTO.getTargetAccount()).orElseThrow(() -> new IllegalArgumentException("Account not found"));

            // Cast the account
            switch (account.getAccountType()) {
                case CHECKING:
                    account = new CurrentCheckingAccount();
                    break;
                case CREDIT:
                    account = account;
                    break;
                case SAVINGS:
                    account = (CurrentSavingsAccount) account;
                    break;
                case STUDENT_CHECKING:
                    account = (CurrentStudentCheckingAccount) account;
                    break;
            }

            // If it matches, create the transaction
            var amount = Money.fromDTO(transactionDTO.getAmount());
            var amountToBeAdded = account.getBalance().increaseAmount(amount);
            account.setBalance(new Money(amountToBeAdded));
            var accountDTO = AccountDTO.fromEntity(account);
            accountService.update(accountDTO.getIban(), accountDTO);

            var targetAccountDTO = accountService.findByIban(transactionDTO.getTargetAccount());
            var primaryOwnerDTO = accountHolderService.findById(targetAccountDTO.getPrimaryOwner());
            var primaryOwner = AccountHolder.fromDTO(primaryOwnerDTO);
            var secondaryOwnerDTO = accountHolderService.findById(targetAccountDTO.getSecondaryOwner());
            var secondaryOwner = AccountHolder.fromDTO(secondaryOwnerDTO);
            var targetAccount = Account.fromDTO(targetAccountDTO, primaryOwner, secondaryOwner);

            var transaction = Transaction.fromDTO(transactionDTO, null, targetAccount);
            transaction.setStatus(TransactionStatus.COMPLETED);
            transaction = transactionRepository.save(transaction);

            return TransactionDTO.fromEntity(transaction);

        } else if (transactionDTO.getHashedKey() != null && transactionDTO.getSourceAccount() != null) { // It's sent by an account holder to a third party

            // Secret key is not needed in this case
            var account = accountService.findById(transactionDTO.getSourceAccount()).orElseThrow(() -> new IllegalArgumentException("Account not found"));

            // Check if the account has sufficient funds
            if (account.getBalance().getAmount().compareTo(transactionDTO.getAmount().getAmount()) < 0) {
                throw new RuntimeException("Insufficient funds");
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
            var secondaryOwnerDTO = accountHolderService.findById(sourceAccountDTO.getSecondaryOwner());
            var secondaryOwner = AccountHolder.fromDTO(secondaryOwnerDTO);
            var sourceAccount = Account.fromDTO(sourceAccountDTO, primaryOwner, secondaryOwner);

            var transaction = Transaction.fromDTO(transactionDTO, sourceAccount, null);
            transaction.setStatus(TransactionStatus.COMPLETED);
            transaction = transactionRepository.save(transaction);

            return TransactionDTO.fromEntity(transaction);
        } else { // it's sent by an account holder to another account holder

            // Check if the accounts are the same
            if (transactionDTO.getSourceAccount().equals(transactionDTO.getTargetAccount())) {
                throw new RuntimeException("You can't send money to the same account");
            }

            // Secret key is not needed in this case
            var senderAccount = accountService.findById(transactionDTO.getSourceAccount()).orElseThrow(() -> new IllegalArgumentException("Account not found"));
            var receiverAccount = accountService.findById(transactionDTO.getTargetAccount()).orElseThrow(() -> new IllegalArgumentException("Account not found"));

            // Check if the sender account has sufficient funds
            if (senderAccount.getBalance().getAmount().compareTo(transactionDTO.getAmount().getAmount()) < 0) {
                throw new RuntimeException("Insufficient funds");
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
            var transaction = Transaction.fromDTO(transactionDTO, senderAccount, receiverAccount);
            transaction.setStatus(TransactionStatus.COMPLETED);
            transaction = transactionRepository.save(transaction);

            return TransactionDTO.fromEntity(transaction);
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
}
