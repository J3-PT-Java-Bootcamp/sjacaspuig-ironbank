package com.ironhack.ironbank.service;

import com.ironhack.ironbank.dto.CurrentStudentCheckingAccountDTO;
import com.ironhack.ironbank.model.account.CurrentStudentCheckingAccount;
import com.ironhack.ironbank.model.user.AccountHolder;
import com.ironhack.ironbank.repository.AccountRepository;
import com.ironhack.ironbank.repository.CurrentStudentCheckingAccountRepository;
import com.ironhack.ironbank.utils.IbanGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CurrentStudentCheckingAccountServiceImpl implements CurrentStudentCheckingAccountService {

    private final CurrentStudentCheckingAccountRepository currentStudentCheckingAccountRepository;
    private final AccountHolderService accountHolderService;
    private final AccountRepository accountRepository;
    private final IbanGenerator ibanGenerator;

    @Override
    public CurrentStudentCheckingAccountDTO create(CurrentStudentCheckingAccountDTO currentStudentCheckingAccountDTO) {
        if(currentStudentCheckingAccountDTO.getIban() != null && currentStudentCheckingAccountRepository.findById(currentStudentCheckingAccountDTO.getIban()).isPresent()) {
            throw new IllegalArgumentException("Student checking account already exists");
        }
        if(currentStudentCheckingAccountDTO.getBalance().getAmount().compareTo(new BigDecimal("0")) < 0) {
            throw new IllegalArgumentException("Balance cannot be negative");
        }

        // Generate iban, check if it exists on accounts, if it does, generate another one, if not, save it
        String iban = ibanGenerator.generateIban();
        while (accountRepository.findById(iban).isPresent()) {
            iban = ibanGenerator.generateIban();
        }
        currentStudentCheckingAccountDTO.setIban(iban);

        var primaryOwner = accountHolderService.findOwnerById(currentStudentCheckingAccountDTO.getPrimaryOwner());
        AccountHolder secondaryOwner = null;
        if(currentStudentCheckingAccountDTO.getSecondaryOwner() != null) {
            secondaryOwner = accountHolderService.findOwnerById(currentStudentCheckingAccountDTO.getSecondaryOwner());
        }
        var currentStudentCheckingAccount = CurrentStudentCheckingAccount.fromDTO(currentStudentCheckingAccountDTO, primaryOwner, secondaryOwner);
        currentStudentCheckingAccount = currentStudentCheckingAccountRepository.save(currentStudentCheckingAccount);
        return CurrentStudentCheckingAccountDTO.fromEntity(currentStudentCheckingAccount);
    }

    @Override
    public CurrentStudentCheckingAccountDTO findByIban(String iban) {
        var currentStudentCheckingAccount = findEntity(iban).orElseThrow(() -> new IllegalArgumentException("Student checking account not found"));
        return CurrentStudentCheckingAccountDTO.fromEntity(currentStudentCheckingAccount);
    }

    @Override
    public Optional<CurrentStudentCheckingAccount> findEntity(String iban) {
        return currentStudentCheckingAccountRepository.findById(iban);
    }

    @Override
    public List<CurrentStudentCheckingAccountDTO> findAll() {
        var currentStudentCheckingAccounts = findAllEntities();
        return CurrentStudentCheckingAccountDTO.fromList(currentStudentCheckingAccounts);
    }

    @Override
    public List<CurrentStudentCheckingAccount> findAllEntities() {
        return currentStudentCheckingAccountRepository.findAll();
    }

    @Override
    public CurrentStudentCheckingAccountDTO update(String iban, CurrentStudentCheckingAccountDTO currentStudentCheckingAccountDTO) {
        var currentStudentCheckingAccount = currentStudentCheckingAccountRepository.findById(iban).orElseThrow(() -> new IllegalArgumentException("Student checking account not found"));
        var currentStudentCheckingAccountUpdated = CurrentStudentCheckingAccount.fromDTO(currentStudentCheckingAccountDTO, currentStudentCheckingAccount.getPrimaryOwner(), currentStudentCheckingAccount.getSecondaryOwner());
        currentStudentCheckingAccountUpdated.setIban(currentStudentCheckingAccount.getIban());
        currentStudentCheckingAccountUpdated = currentStudentCheckingAccountRepository.save(currentStudentCheckingAccountUpdated);
        return CurrentStudentCheckingAccountDTO.fromEntity(currentStudentCheckingAccountUpdated);
    }

    @Override
    public void delete(String iban) {
        currentStudentCheckingAccountRepository.deleteById(iban);
    }
}
