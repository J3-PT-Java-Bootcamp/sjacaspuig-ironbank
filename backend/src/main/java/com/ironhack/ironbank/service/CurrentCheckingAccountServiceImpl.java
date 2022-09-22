package com.ironhack.ironbank.service;

import com.ironhack.ironbank.constants.AccountConstants;
import com.ironhack.ironbank.dto.AccountDTO;
import com.ironhack.ironbank.dto.AccountStatusDTO;
import com.ironhack.ironbank.dto.CurrentCheckingAccountDTO;
import com.ironhack.ironbank.dto.CurrentStudentCheckingAccountDTO;
import com.ironhack.ironbank.enums.TransactionStatus;
import com.ironhack.ironbank.enums.TransactionType;
import com.ironhack.ironbank.model.Money;
import com.ironhack.ironbank.model.Transaction;
import com.ironhack.ironbank.model.account.Account;
import com.ironhack.ironbank.model.account.CurrentCheckingAccount;
import com.ironhack.ironbank.model.user.AccountHolder;
import com.ironhack.ironbank.repository.AccountRepository;
import com.ironhack.ironbank.repository.CurrentCheckingAccountRepository;
import com.ironhack.ironbank.repository.TransactionRepository;
import com.ironhack.ironbank.utils.DateService;
import com.ironhack.ironbank.utils.IbanGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CurrentCheckingAccountServiceImpl implements CurrentCheckingAccountService {

    private final CurrentCheckingAccountRepository currentCheckingAccountRepository;
    private final AccountHolderService accountHolderService;
    private final AccountRepository accountRepository;
    private final IbanGenerator ibanGenerator;
    private final TransactionRepository transactionRepository;
    private final CurrentStudentCheckingAccountService currentStudentCheckingAccountService;

    @Override
    public AccountDTO create(CurrentCheckingAccountDTO currentCheckingAccountDTO) {
        if (currentCheckingAccountDTO.getIban() != null && currentCheckingAccountRepository.findById(currentCheckingAccountDTO.getIban()).isPresent()) {
            throw new IllegalArgumentException("Checking account with IBAN " + currentCheckingAccountDTO.getIban() + " already exists");
        }
        if (currentCheckingAccountDTO.getBalance().getAmount().compareTo(new BigDecimal("0")) < 0) {
            throw new IllegalArgumentException("Balance cannot of the checking account with IBAN " + currentCheckingAccountDTO.getIban() + " be negative");
        }
        if (currentCheckingAccountDTO.getBalance().getAmount().compareTo(CurrentCheckingAccount.MINIMUM_BALANCE.getAmount()) < 0) {
            throw new IllegalArgumentException("Balance cannot of the checking account with IBAN " + currentCheckingAccountDTO.getIban() + " be less than " + CurrentCheckingAccount.MINIMUM_BALANCE.getAmount());
        }

        // Generate iban, check if it exists on accounts, if it does, generate another one, if not, save it
        String iban = ibanGenerator.generateIban();
        while (accountRepository.findById(iban).isPresent()) {
            iban = ibanGenerator.generateIban();
        }
        currentCheckingAccountDTO.setIban(iban);

        var primaryOwner = accountHolderService.findOwnerById(currentCheckingAccountDTO.getPrimaryOwner());
        AccountHolder secondaryOwner = null;
        if (currentCheckingAccountDTO.getSecondaryOwner() != null) {
            secondaryOwner = accountHolderService.findOwnerById(currentCheckingAccountDTO.getSecondaryOwner());
        }

        try {

            // Try to create a new checking account
            var currentCheckingAccount = CurrentCheckingAccount.fromDTO(currentCheckingAccountDTO, primaryOwner, secondaryOwner);
            currentCheckingAccount = currentCheckingAccountRepository.save(currentCheckingAccount);
            return CurrentCheckingAccountDTO.fromEntity(currentCheckingAccount);
        } catch (Exception e) {

            // Case: The account holder is younger than 24 years old, we will create a student checking account
            if (e.getMessage().contains(AccountConstants.PRIMARY_OWNER_YOUNGER_THAN_24)) {
                var studentAccountDTO = CurrentStudentCheckingAccountDTO.fromCurrentAccountDTO(currentCheckingAccountDTO);
                studentAccountDTO.setPrimaryOwner(primaryOwner.getId());
                if (secondaryOwner != null) {
                    studentAccountDTO.setSecondaryOwner(secondaryOwner.getId());
                }
                var studentAccount = currentStudentCheckingAccountService.create(studentAccountDTO);
                return studentAccount;
            }
        }
        var currentCheckingAccount = CurrentCheckingAccount.fromDTO(currentCheckingAccountDTO, primaryOwner, secondaryOwner);
        currentCheckingAccount = currentCheckingAccountRepository.save(currentCheckingAccount);
        return CurrentCheckingAccountDTO.fromEntity(currentCheckingAccount);
    }

    @Override
    public CurrentCheckingAccountDTO findByIban(String iban) {
        var currentCheckingAccount = findEntity(iban).orElseThrow(() -> new IllegalArgumentException("Checking account with IBAN " + iban + " does not exist"));
        return CurrentCheckingAccountDTO.fromEntity(currentCheckingAccount);
    }

    @Override
    public Optional<CurrentCheckingAccount> findEntity(String iban) {
        var currentCheckingAccount = currentCheckingAccountRepository.findById(iban);
        if (currentCheckingAccount.isPresent()) {
            currentCheckingAccount = Optional.of(applyMonthlyMaintenanceFee(currentCheckingAccount.get()));
        }
        return currentCheckingAccount;
    }

    @Override
    public List<CurrentCheckingAccountDTO> findAll() {
        var currentCheckingAccounts = findAllEntities();
        return CurrentCheckingAccountDTO.fromList(currentCheckingAccounts);
    }

    @Override
    public List<CurrentCheckingAccount> findAllEntities() {
        var currentCheckingAccounts = currentCheckingAccountRepository.findAll();

        //Apply monthly maintenance fee to all accounts
        for(int i = 0; i < currentCheckingAccounts.size(); i++) {
            currentCheckingAccounts.set(i, applyMonthlyMaintenanceFee(currentCheckingAccounts.get(i)));
        }

        return currentCheckingAccounts;
    }

    @Override
    public CurrentCheckingAccountDTO update(String iban, CurrentCheckingAccountDTO currentCheckingAccountDTO) {
        var currentCheckingAccount = currentCheckingAccountRepository.findById(iban).orElseThrow(() -> new IllegalArgumentException("Checking account with IBAN " + iban + " does not exist"));
        var currentCheckingAccountUpdated = CurrentCheckingAccount.fromDTO(currentCheckingAccountDTO, currentCheckingAccount.getPrimaryOwner(), currentCheckingAccount.getSecondaryOwner());
        currentCheckingAccountUpdated.setIban(currentCheckingAccount.getIban());

        // Check if balance is less than minimum balance
        currentCheckingAccountUpdated = checkBalance(currentCheckingAccountUpdated);

        currentCheckingAccountUpdated = currentCheckingAccountRepository.save(currentCheckingAccountUpdated);
        return CurrentCheckingAccountDTO.fromEntity(currentCheckingAccountUpdated);
    }

    @Override
    public void delete(String iban) {
        currentCheckingAccountRepository.deleteById(iban);
    }

    @Override
    public CurrentCheckingAccountDTO changeStatus(@Valid String iban, @Valid AccountStatusDTO accountStatusDTO) {
        var currentCheckingAccount = currentCheckingAccountRepository.findById(iban).orElseThrow(() -> new IllegalArgumentException("Checking account with IBAN " + iban + " does not exist"));
        currentCheckingAccount.setStatus(accountStatusDTO.getStatus());
        currentCheckingAccount = currentCheckingAccountRepository.save(currentCheckingAccount);
        return CurrentCheckingAccountDTO.fromEntity(currentCheckingAccount);
    }

    @Override
    public List<CurrentCheckingAccountDTO> findByAccountHolderId(String id) {
        var currentCheckingAccounts = currentCheckingAccountRepository.findAllByPrimaryOwnerIdOrSecondaryOwnerId(id, id);
        return CurrentCheckingAccountDTO.fromList(currentCheckingAccounts);
    }

    private CurrentCheckingAccount checkBalance(CurrentCheckingAccount currentCheckingAccount) {
        if (currentCheckingAccount.getBalance().getAmount().compareTo(CurrentCheckingAccount.MINIMUM_BALANCE.getAmount()) < 0) {
            var balanceUpdated = currentCheckingAccount.getBalance().decreaseAmount(Account.PENALTY_FEE);
            currentCheckingAccount.setBalance(new Money(balanceUpdated));

            createPenaltyMinBalanceTransaction(currentCheckingAccount);
        }
        return currentCheckingAccount;
    }

    private CurrentCheckingAccount applyMonthlyMaintenanceFee(CurrentCheckingAccount currentCheckingAccount) {
        int diffMonths = 0;

        if(currentCheckingAccount.getLastMonthlyFeeDate() != null) {
            diffMonths = DateService.getDiffMonths(currentCheckingAccount.getLastMonthlyFeeDate());
        }

        if (diffMonths >= 1) {
            var fees = CurrentCheckingAccount.MONTHLY_MAINTENANCE_FEE.getAmount().multiply(new BigDecimal(diffMonths));
            var balanceUpdated = currentCheckingAccount.getBalance().decreaseAmount(fees);
            currentCheckingAccount.setBalance(new Money(balanceUpdated));
            currentCheckingAccount.setLastMonthlyFeeDate(Instant.now());
            var checkingAccountDTO = CurrentCheckingAccountDTO.fromEntity(currentCheckingAccount);
            update(currentCheckingAccount.getIban(), checkingAccountDTO);

            createMaintenanceFeeTransaction(currentCheckingAccount, balanceUpdated, diffMonths);
        }

        return currentCheckingAccount;
    }

    private void createPenaltyMinBalanceTransaction(Account account) {
        var transaction = new Transaction();
        transaction.setSourceAccount(account);
        transaction.setAmount(Account.PENALTY_FEE);
        transaction.setName(account.getPrimaryOwner().getFirstName() + " " + account.getPrimaryOwner().getLastName());
        transaction.setConcept("Penalty fee of " + Account.PENALTY_FEE.getAmount() + " for having a balance below " + CurrentCheckingAccount.MINIMUM_BALANCE.getAmount());
        transaction.setStatus(TransactionStatus.COMPLETED);
        transaction.setType(TransactionType.PENALTY_MIN_BALANCE);
        transactionRepository.save(transaction);
    }

    private void createMaintenanceFeeTransaction(Account account, BigDecimal balanceUpdated, int diffMonths) {
        var transaction = new Transaction();
        transaction.setSourceAccount(account);
        transaction.setAmount(new Money(balanceUpdated));
        transaction.setName(account.getPrimaryOwner().getFirstName() + " " + account.getPrimaryOwner().getLastName());
        transaction.setConcept("Monthly maintenance fee for " + diffMonths + " months");
        transaction.setStatus(TransactionStatus.COMPLETED);
        transaction.setType(TransactionType.MAINTENANCE_FEE);
        transactionRepository.save(transaction);
    }
}
