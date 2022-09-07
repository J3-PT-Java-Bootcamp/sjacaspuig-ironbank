package com.ironhack.ironbank.service;

import com.ironhack.ironbank.dto.CurrentCheckingAccountDTO;
import com.ironhack.ironbank.model.account.CurrentCheckingAccount;
import com.ironhack.ironbank.model.user.AccountHolder;
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
        return null;
    }

    @Override
    public List<CurrentCheckingAccountDTO> findAll() {
        return null;
    }

    @Override
    public CurrentCheckingAccountDTO update(String iban, CurrentCheckingAccountDTO currentCheckingAccountDTO) {
        return null;
    }

    @Override
    public void delete(String iban) {

    }
}
