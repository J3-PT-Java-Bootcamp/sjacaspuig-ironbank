package com.ironhack.ironbank.service;

import com.ironhack.ironbank.dto.CurrentCheckingAccountDTO;
import com.ironhack.ironbank.model.account.CurrentCheckingAccount;
import com.ironhack.ironbank.repository.CurrentCheckingAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CurrentCheckingAccountServiceImpl implements CurrentCheckingAccountService {

    private final CurrentCheckingAccountRepository currentCheckingAccountRepository;
    private final AccountHolderService accountHolderService;

    @Override
    public CurrentCheckingAccountDTO create(CurrentCheckingAccountDTO currentCheckingAccountDTO) {
        if (currentCheckingAccountDTO.getIban() != null && currentCheckingAccountRepository.findById(currentCheckingAccountDTO.getIban()).isPresent()) {
            throw new IllegalArgumentException("Checking account already exists");
        }

        var primaryOwner = accountHolderService.findOwnerById(currentCheckingAccountDTO.getPrimaryOwner());
        var secondaryOwner = accountHolderService.findOwnerById(currentCheckingAccountDTO.getSecondaryOwner());
        var currentCheckingAccount = CurrentCheckingAccount.fromDTO(currentCheckingAccountDTO, primaryOwner, secondaryOwner);
        currentCheckingAccount = currentCheckingAccountRepository.save(currentCheckingAccount);
        return CurrentCheckingAccountDTO.fromEntity(currentCheckingAccount);
    }

    @Override
    public CurrentCheckingAccountDTO findByIban(String iban) {
        var currentCheckingAccount = currentCheckingAccountRepository.findById(iban).orElseThrow(() -> new IllegalArgumentException("Checking account not found"));
        return CurrentCheckingAccountDTO.fromEntity(currentCheckingAccount);
    }

    @Override
    public List<CurrentCheckingAccountDTO> findAll() {
        var currentCheckingAccounts = currentCheckingAccountRepository.findAll();
        return CurrentCheckingAccountDTO.fromList(currentCheckingAccounts);
    }

    @Override
    public CurrentCheckingAccountDTO update(String iban, CurrentCheckingAccountDTO currentCheckingAccountDTO) {
        var currentCheckingAccount = currentCheckingAccountRepository.findById(iban).orElseThrow(() -> new IllegalArgumentException("Checking account not found"));
        var currentCheckingAccountUpdated = CurrentCheckingAccount.fromDTO(currentCheckingAccountDTO, currentCheckingAccount.getPrimaryOwner(), currentCheckingAccount.getSecondaryOwner());
        currentCheckingAccountUpdated.setIban(currentCheckingAccount.getIban());
        currentCheckingAccountUpdated = currentCheckingAccountRepository.save(currentCheckingAccountUpdated);
        return CurrentCheckingAccountDTO.fromEntity(currentCheckingAccountUpdated);
    }

    @Override
    public void delete(String iban) {
        currentCheckingAccountRepository.deleteById(iban);
    }
}
