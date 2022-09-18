package com.ironhack.ironbank.service;

import com.ironhack.ironbank.constants.AccountConstants;
import com.ironhack.ironbank.dto.CreditAccountDTO;
import com.ironhack.ironbank.dto.response.InterestRateResponse;
import com.ironhack.ironbank.enums.TransactionStatus;
import com.ironhack.ironbank.enums.TransactionType;
import com.ironhack.ironbank.model.Money;
import com.ironhack.ironbank.model.Transaction;
import com.ironhack.ironbank.model.account.Account;
import com.ironhack.ironbank.model.account.CreditAccount;
import com.ironhack.ironbank.model.user.AccountHolder;
import com.ironhack.ironbank.repository.AccountRepository;
import com.ironhack.ironbank.repository.CreditAccountRepository;
import com.ironhack.ironbank.repository.TransactionRepository;
import com.ironhack.ironbank.utils.DateService;
import com.ironhack.ironbank.utils.IbanGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CreditAccountServiceImpl implements CreditAccountService {

    private final CreditAccountRepository creditAccountRepository;
    private final AccountHolderService accountHolderService;
    private final AccountRepository accountRepository;
    private final IbanGenerator ibanGenerator;
    private final TransactionRepository transactionRepository;

    @Override
    public CreditAccountDTO create(CreditAccountDTO creditAccountDTO) {
        if (creditAccountDTO.getIban() != null && creditAccountRepository.findById(creditAccountDTO.getIban()).isPresent()) {
            throw new IllegalArgumentException("Credit account already exists");
        }
        if (creditAccountDTO.getBalance().getAmount().compareTo(new BigDecimal("0")) < 0) {
            throw new IllegalArgumentException("Balance cannot be negative");
        }

        // Generate iban, check if it exists on accounts, if it does, generate another one, if not, save it
        String iban = ibanGenerator.generateIban();
        while (accountRepository.findById(iban).isPresent()) {
            iban = ibanGenerator.generateIban();
        }
        creditAccountDTO.setIban(iban);

        var primaryOwner = accountHolderService.findOwnerById(creditAccountDTO.getPrimaryOwner());

        AccountHolder secondaryOwner = null;
        if (creditAccountDTO.getSecondaryOwner() != null) {
            secondaryOwner = accountHolderService.findOwnerById(creditAccountDTO.getSecondaryOwner());
        }
        var creditAccount = CreditAccount.fromDTO(creditAccountDTO, primaryOwner, secondaryOwner);
        creditAccount = creditAccountRepository.save(creditAccount);
        return CreditAccountDTO.fromEntity(creditAccount);
    }

    @Override
    public CreditAccountDTO findByIban(String iban) {
        var creditAccount = findEntity(iban).orElseThrow(() -> new IllegalArgumentException("Credit account not found"));
        return CreditAccountDTO.fromEntity(creditAccount);
    }

    @Override
    public Optional<CreditAccount> findEntity(String iban) {
        var creditAccount = creditAccountRepository.findById(iban);

        if (creditAccount.isPresent()) {
            creditAccount = Optional.of(applyInterest(creditAccount.get()));
        }

        return creditAccount;
    }

    @Override
    public List<CreditAccountDTO> findAll() {
        var creditAccounts = findAllEntities();
        return CreditAccountDTO.fromList(creditAccounts);
    }

    @Override
    public List<CreditAccount> findAllEntities() {
        var creditAccounts = creditAccountRepository.findAll();

        // Apply interest to all credit accounts
        for (int i = 0; i < creditAccounts.size(); i++) {
            creditAccounts.set(i, applyInterest(creditAccounts.get(i)));
        }

        return creditAccounts;
    }

    @Override
    public CreditAccountDTO update(String iban, CreditAccountDTO creditAccountDTO) {
        var creditAccount = creditAccountRepository.findById(iban).orElseThrow(() -> new IllegalArgumentException("Credit account not found"));
        var creditAccountUpdated = CreditAccount.fromDTO(creditAccountDTO, creditAccount.getPrimaryOwner(), creditAccount.getSecondaryOwner());
        creditAccountUpdated.setIban(creditAccount.getIban());

        // Check if the balance is less than the global minimum balance
        creditAccountUpdated = checkBalance(creditAccountUpdated);

        creditAccountUpdated = creditAccountRepository.save(creditAccountUpdated);
        return CreditAccountDTO.fromEntity(creditAccountUpdated);
    }

    @Override
    public void delete(String iban) {
        creditAccountRepository.deleteById(iban);
    }

    private CreditAccount applyInterest(CreditAccount creditAccount) {
        var interestRate = getInterestEarnings(creditAccount);

        if(interestRate.getTimesApplied() > 0) {
            // Add the interest rate to the account and update it
            creditAccount.setBalance(new Money(creditAccount.getBalance().increaseAmount(interestRate.getEarnings())));
            creditAccount.setInterestRateDate(Instant.now());
            var creditAccountDTO = CreditAccountDTO.fromEntity(creditAccount);
            update(creditAccount.getIban(), creditAccountDTO);

            // Create the transaction
            createInterestTransaction(creditAccount, interestRate);
        }

        return creditAccount;
    }
    private InterestRateResponse getInterestEarnings(CreditAccount creditAccount) {
        int diffMonths = 0;
        Money interestEarnings = new Money(new BigDecimal("0"));
        BigDecimal interestRateApplied = null;

        if(creditAccount.getInterestRateDate() != null) {
            diffMonths = DateService.getDiffMonths(creditAccount.getInterestRateDate());
        }

        if (diffMonths >= 1) {
            var interestRate = creditAccount.getInterestRate();
            interestRateApplied = interestRate.divide(new BigDecimal("12"), 2, RoundingMode.HALF_UP);
            interestRateApplied = interestRateApplied.multiply(new BigDecimal(diffMonths));
            interestEarnings = new Money(creditAccount.getBalance().getAmount().multiply(interestRateApplied));
        }

        return new InterestRateResponse(interestRateApplied, interestEarnings, diffMonths);
    }

    private CreditAccount checkBalance(CreditAccount creditAccount) {
        if (creditAccount.getBalance().getAmount().compareTo(AccountConstants.GLOBAL_MINIMUM_BALANCE.getAmount()) < 0) {
            var balanceUpdated = creditAccount.getBalance().decreaseAmount(Account.PENALTY_FEE);
            creditAccount.setBalance(new Money(balanceUpdated));

            createPenaltyMinBalanceTransaction(creditAccount);
        }
        return creditAccount;
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

}
