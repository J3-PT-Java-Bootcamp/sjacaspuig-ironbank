package com.ironhack.ironbank.service;

import com.ironhack.ironbank.dto.CurrentStudentCheckingAccountDTO;
import com.ironhack.ironbank.model.account.CurrentStudentCheckingAccount;
import com.ironhack.ironbank.repository.CurrentStudentCheckingAccountRepository;
import com.ironhack.ironbank.utils.IbanGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CurrentStudentCheckingAccountServiceImpl implements CurrentStudentCheckingAccountService {

    private final CurrentStudentCheckingAccountRepository currentStudentCheckingAccountRepository;
    private final AccountHolderService accountHolderService;
    private final AccountService accountService;
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
        while (accountService.findById(iban).isPresent()) {
            iban = ibanGenerator.generateIban();
        }
        currentStudentCheckingAccountDTO.setIban(iban);

        var primaryOwner = accountHolderService.findOwnerById(currentStudentCheckingAccountDTO.getPrimaryOwner());
        var secondaryOwner = accountHolderService.findOwnerById(currentStudentCheckingAccountDTO.getSecondaryOwner());
        var currentStudentCheckingAccount = CurrentStudentCheckingAccount.fromDTO(currentStudentCheckingAccountDTO, primaryOwner, secondaryOwner);
        currentStudentCheckingAccount = currentStudentCheckingAccountRepository.save(currentStudentCheckingAccount);
        return CurrentStudentCheckingAccountDTO.fromEntity(currentStudentCheckingAccount);
    }

    @Override
    public CurrentStudentCheckingAccountDTO findByIban(String iban) {
        var currentStudentCheckingAccount = currentStudentCheckingAccountRepository.findById(iban).orElseThrow(() -> new IllegalArgumentException("Student checking account not found"));
        return CurrentStudentCheckingAccountDTO.fromEntity(currentStudentCheckingAccount);
    }

    @Override
    public List<CurrentStudentCheckingAccountDTO> findAll() {
        var currentStudentCheckingAccounts = currentStudentCheckingAccountRepository.findAll();
        return CurrentStudentCheckingAccountDTO.fromList(currentStudentCheckingAccounts);
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
