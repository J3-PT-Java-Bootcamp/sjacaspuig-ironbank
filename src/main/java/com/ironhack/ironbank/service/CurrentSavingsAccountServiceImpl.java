package com.ironhack.ironbank.service;

import com.ironhack.ironbank.dto.CurrentSavingsAccountDTO;
import com.ironhack.ironbank.model.account.CurrentSavingsAccount;
import com.ironhack.ironbank.repository.CurrentSavingsAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CurrentSavingsAccountServiceImpl implements CurrentSavingsAccountService {

    private final CurrentSavingsAccountRepository currentSavingsAccountRepository;
    private final AccountHolderService accountHolderService;

    @Override
    public CurrentSavingsAccountDTO create(CurrentSavingsAccountDTO currentSavingsAccountDTO) {
        if(currentSavingsAccountDTO.getIban() != null && currentSavingsAccountRepository.findById(currentSavingsAccountDTO.getIban()).isPresent()) {
            throw new IllegalArgumentException("Savings account already exists");
        }

        var primaryOwner = accountHolderService.findOwnerById(currentSavingsAccountDTO.getPrimaryOwner());
        var secondaryOwner = accountHolderService.findOwnerById(currentSavingsAccountDTO.getSecondaryOwner());
        var currentSavingsAccount = CurrentSavingsAccount.fromDTO(currentSavingsAccountDTO, primaryOwner, secondaryOwner);
        currentSavingsAccount = currentSavingsAccountRepository.save(currentSavingsAccount);
        return CurrentSavingsAccountDTO.fromEntity(currentSavingsAccount);
    }

    @Override
    public CurrentSavingsAccountDTO findByIban(String iban) {
        return null;
    }

    @Override
    public List<CurrentSavingsAccountDTO> findAll() {
        return null;
    }

    @Override
    public CurrentSavingsAccountDTO update(String iban, CurrentSavingsAccountDTO currentSavingsAccountDTO) {
        return null;
    }

    @Override
    public void delete(String iban) {

    }
}
