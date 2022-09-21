package com.ironhack.ironbank.service;

import com.ironhack.ironbank.dto.AccountStatusDTO;
import com.ironhack.ironbank.dto.CurrentSavingsAccountDTO;
import com.ironhack.ironbank.dto.response.InterestRateResponse;
import com.ironhack.ironbank.enums.TransactionStatus;
import com.ironhack.ironbank.enums.TransactionType;
import com.ironhack.ironbank.model.MyDecimal;
import com.ironhack.ironbank.model.Money;
import com.ironhack.ironbank.model.Transaction;
import com.ironhack.ironbank.model.account.Account;
import com.ironhack.ironbank.model.account.CurrentSavingsAccount;
import com.ironhack.ironbank.model.user.AccountHolder;
import com.ironhack.ironbank.repository.AccountRepository;
import com.ironhack.ironbank.repository.CurrentSavingsAccountRepository;
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
public class CurrentSavingsAccountServiceImpl implements CurrentSavingsAccountService {

    private final CurrentSavingsAccountRepository currentSavingsAccountRepository;
    private final AccountHolderService accountHolderService;
    private final AccountRepository accountRepository;
    private final IbanGenerator ibanGenerator;
    private final TransactionRepository transactionRepository;

    @Override
    public CurrentSavingsAccountDTO create(CurrentSavingsAccountDTO currentSavingsAccountDTO) {
        if(currentSavingsAccountDTO.getIban() != null && currentSavingsAccountRepository.findById(currentSavingsAccountDTO.getIban()).isPresent()) {
            throw new IllegalArgumentException("Savings account already exists");
        }
        if(currentSavingsAccountDTO.getBalance().getAmount().compareTo(new BigDecimal("0")) < 0) {
            throw new IllegalArgumentException("Balance cannot be negative");
        }

        BigDecimal minimumBalance = currentSavingsAccountDTO.getMinimumBalance() != null ? currentSavingsAccountDTO.getMinimumBalance().getAmount() : CurrentSavingsAccount.DEFAULT_MINIMUM_BALANCE.getAmount();
        if(currentSavingsAccountDTO.getBalance().getAmount().compareTo(minimumBalance) < 0) {
            throw new IllegalArgumentException("Balance cannot be less than the minimum balance");
        }

        // Generate iban, check if it exists on accounts, if it does, generate another one, if not, save it
        String iban = ibanGenerator.generateIban();
        while (accountRepository.findById(iban).isPresent()) {
            iban = ibanGenerator.generateIban();
        }
        currentSavingsAccountDTO.setIban(iban);

        var primaryOwner = accountHolderService.findOwnerById(currentSavingsAccountDTO.getPrimaryOwner());
        AccountHolder secondaryOwner = null;
        if(currentSavingsAccountDTO.getSecondaryOwner() != null) {
            secondaryOwner = accountHolderService.findOwnerById(currentSavingsAccountDTO.getSecondaryOwner());
        }
        var currentSavingsAccount = CurrentSavingsAccount.fromDTO(currentSavingsAccountDTO, primaryOwner, secondaryOwner);
        currentSavingsAccount = currentSavingsAccountRepository.save(currentSavingsAccount);
        return CurrentSavingsAccountDTO.fromEntity(currentSavingsAccount);
    }

    @Override
    public CurrentSavingsAccountDTO findByIban(String iban) {
        var currentSavingsAccount = findEntity(iban).orElseThrow(() -> new IllegalArgumentException("Savings account not found"));
        return CurrentSavingsAccountDTO.fromEntity(currentSavingsAccount);
    }

    @Override
    public Optional<CurrentSavingsAccount> findEntity(String iban) {
        var currentSavingsAccount = currentSavingsAccountRepository.findById(iban);
        if(currentSavingsAccount.isPresent()) {
            currentSavingsAccount = Optional.of(applyInterest(currentSavingsAccount.get()));
        }
        return currentSavingsAccount;
    }

    @Override
    public List<CurrentSavingsAccountDTO> findAll() {
        var currentSavingsAccounts = findAllEntities();
        return CurrentSavingsAccountDTO.fromList(currentSavingsAccounts);
    }

    @Override
    public List<CurrentSavingsAccount> findAllEntities() {
        var currentSavingsAccounts = currentSavingsAccountRepository.findAll();
        // Apply interest to all accounts
        for(int i = 0; i < currentSavingsAccounts.size(); i++) {
            currentSavingsAccounts.set(i, applyInterest(currentSavingsAccounts.get(i)));
        }
        return currentSavingsAccounts;
    }

    @Override
    public CurrentSavingsAccountDTO update(String iban, CurrentSavingsAccountDTO currentSavingsAccountDTO) {
        var currentSavingsAccount = currentSavingsAccountRepository.findById(iban).orElseThrow(() -> new IllegalArgumentException("Savings account not found"));
        var currentSavingsAccountUpdated = CurrentSavingsAccount.fromDTO(currentSavingsAccountDTO, currentSavingsAccount.getPrimaryOwner(), currentSavingsAccount.getSecondaryOwner());
        currentSavingsAccountUpdated.setIban(currentSavingsAccount.getIban());

        // Check if balance is less than minimum balance
        currentSavingsAccountUpdated = checkBalance(currentSavingsAccountUpdated);

        currentSavingsAccountUpdated = currentSavingsAccountRepository.save(currentSavingsAccountUpdated);
        return CurrentSavingsAccountDTO.fromEntity(currentSavingsAccountUpdated);
    }

    @Override
    public void delete(String iban) {
        currentSavingsAccountRepository.deleteById(iban);
    }

    @Override
    public CurrentSavingsAccountDTO changeStatus(@Valid String iban, @Valid AccountStatusDTO accountStatusDTO) {
        var currentSavingsAccount = currentSavingsAccountRepository.findById(iban).orElseThrow(() -> new IllegalArgumentException("Savings account not found"));
        currentSavingsAccount.setStatus(accountStatusDTO.getStatus());
        currentSavingsAccount = currentSavingsAccountRepository.save(currentSavingsAccount);
        return CurrentSavingsAccountDTO.fromEntity(currentSavingsAccount);
    }

    @Override
    public List<CurrentSavingsAccountDTO> findByAccountHolderId(String id) {
        var currentSavingsAccounts = currentSavingsAccountRepository.findAllByPrimaryOwnerIdOrSecondaryOwnerId(id, id);
        return CurrentSavingsAccountDTO.fromList(currentSavingsAccounts);
    }

    private CurrentSavingsAccount applyInterest(CurrentSavingsAccount savingsAccount) {
        var interestRate = getInterestEarnings(savingsAccount);

        if(interestRate.getTimesApplied() > 0) {
            // Add the interest rate to the account and update it
            savingsAccount.setBalance(new Money(savingsAccount.getBalance().increaseAmount(interestRate.getEarnings())));
            savingsAccount.setInterestRateDate(Instant.now());
            var savingsAccountDTO = CurrentSavingsAccountDTO.fromEntity(savingsAccount);
            update(savingsAccount.getIban(), savingsAccountDTO);

            // Create the transaction
            createInterestTransaction(savingsAccount, interestRate);
        }

        return savingsAccount;
    }

    private InterestRateResponse getInterestEarnings(CurrentSavingsAccount savingsAccount) {
        int diffYears = 0;
        Money interestEarnings = new Money(new BigDecimal("0"));
        BigDecimal interestRateApplied = null;

        if(savingsAccount.getInterestRateDate() != null) {
            diffYears = DateService.getDiffYears(savingsAccount.getInterestRateDate());
        } else {
            diffYears = DateService.getDiffYears(savingsAccount.getCreationDate());
        }

        if (diffYears >= 1) {
            var interestRate = savingsAccount.getInterestRate();
            var decimal = interestRate.multiply(new MyDecimal(diffYears));
//            interestRateApplied = interestRate.multiply(new BigDecimal(diffYears));
            interestRateApplied = decimal.getValue();
            interestEarnings = new Money(savingsAccount.getBalance().getAmount().multiply(interestRateApplied));
        }

        return new InterestRateResponse(interestRateApplied, interestEarnings, diffYears);
    }

    private CurrentSavingsAccount checkBalance(CurrentSavingsAccount currentSavingsAccount) {
        if (currentSavingsAccount.getBalance().getAmount().compareTo(currentSavingsAccount.getMinimumBalance().getAmount()) < 0) {
            var balanceUpdated = currentSavingsAccount.getBalance().decreaseAmount(Account.PENALTY_FEE);
            currentSavingsAccount.setBalance(new Money(balanceUpdated));

            createPenaltyMinBalanceTransaction(currentSavingsAccount);
        }
        return currentSavingsAccount;
    }

    private void createInterestTransaction(Account account, InterestRateResponse response) {
        var transaction = new Transaction();
        transaction.setTargetAccount(account);
        transaction.setAmount(response.getEarnings());
        transaction.setName(account.getPrimaryOwner().getFirstName() + " " + account.getPrimaryOwner().getLastName());
        transaction.setConcept("For " + response.getTimesApplied() + " periods an interest of " + response.getInterestRateApplied().multiply(new BigDecimal("100")) + "% has been applied.");
        transaction.setStatus(TransactionStatus.COMPLETED);
        transaction.setType(TransactionType.INTEREST);
        transactionRepository.save(transaction);
    }

    private void createPenaltyMinBalanceTransaction(Account account) {
        var transaction = new Transaction();
        transaction.setSourceAccount(account);
        transaction.setAmount(Account.PENALTY_FEE);
        transaction.setName(account.getPrimaryOwner().getFirstName() + " " + account.getPrimaryOwner().getLastName());
        transaction.setConcept("Penalty fee for not having the minimum balance");
        transaction.setStatus(TransactionStatus.COMPLETED);
        transaction.setType(TransactionType.PENALTY_MIN_BALANCE);
        transactionRepository.save(transaction);
    }
}
