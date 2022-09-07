package com.ironhack.ironbank.service;

import com.ironhack.ironbank.dto.CurrentStudentCheckingAccountDTO;
import com.ironhack.ironbank.model.account.CurrentStudentCheckingAccount;
import com.ironhack.ironbank.repository.CurrentStudentCheckingAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CurrentStudentCheckingAccountServiceImpl implements CurrentStudentCheckingAccountService {

    private final CurrentStudentCheckingAccountRepository currentStudentCheckingAccountRepository;
    private final AccountHolderService accountHolderService;

    @Override
    public CurrentStudentCheckingAccountDTO create(CurrentStudentCheckingAccountDTO currentStudentCheckingAccountDTO) {
        if(currentStudentCheckingAccountDTO.getIban() != null && currentStudentCheckingAccountRepository.findById(currentStudentCheckingAccountDTO.getIban()).isPresent()) {
            throw new IllegalArgumentException("Student checking account already exists");
        }

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
        return null;
    }

    @Override
    public void delete(String iban) {

    }
}
