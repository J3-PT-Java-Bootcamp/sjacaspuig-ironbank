package com.ironhack.ironbank.service;

import com.ironhack.ironbank.dto.CurrentSavingsAccountDTO;
import com.ironhack.ironbank.dto.MoneyDTO;
import com.ironhack.ironbank.model.account.CurrentSavingsAccount;
import com.ironhack.ironbank.model.user.AccountHolder;
import com.ironhack.ironbank.repository.CurrentSavingsAccountRepository;
import com.ironhack.ironbank.utils.IbanGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CurrentSavingsAccountServiceImpl implements CurrentSavingsAccountService {

    private final CurrentSavingsAccountRepository currentSavingsAccountRepository;
    private final AccountHolderService accountHolderService;
    private final AccountService accountService;
    private final IbanGenerator ibanGenerator;

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
        while (accountService.findById(iban).isPresent()) {
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
        var currentSavingsAccount = currentSavingsAccountRepository.findById(iban).orElseThrow(() -> new IllegalArgumentException("Savings account not found"));
        return CurrentSavingsAccountDTO.fromEntity(currentSavingsAccount);
    }

    @Override
    public List<CurrentSavingsAccountDTO> findAll() {
        var currentSavingsAccounts = currentSavingsAccountRepository.findAll();
        return CurrentSavingsAccountDTO.fromList(currentSavingsAccounts);
    }

    @Override
    public CurrentSavingsAccountDTO update(String iban, CurrentSavingsAccountDTO currentSavingsAccountDTO) {
        var currentSavingsAccount = currentSavingsAccountRepository.findById(iban).orElseThrow(() -> new IllegalArgumentException("Savings account not found"));
        var currentSavingsAccountUpdated = CurrentSavingsAccount.fromDTO(currentSavingsAccountDTO, currentSavingsAccount.getPrimaryOwner(), currentSavingsAccount.getSecondaryOwner());
        currentSavingsAccountUpdated.setIban(currentSavingsAccount.getIban());
        currentSavingsAccountUpdated = currentSavingsAccountRepository.save(currentSavingsAccountUpdated);
        return CurrentSavingsAccountDTO.fromEntity(currentSavingsAccountUpdated);
    }

    @Override
    public void delete(String iban) {
        currentSavingsAccountRepository.deleteById(iban);
    }
}
