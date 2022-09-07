package com.ironhack.ironbank.service;

import com.ironhack.ironbank.dto.CreditAccountDTO;
import com.ironhack.ironbank.model.account.CreditAccount;
import com.ironhack.ironbank.repository.CreditAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CreditAccountServiceImpl implements CreditAccountService {

    private final CreditAccountRepository creditAccountRepository;
    private final AccountHolderService accountHolderService;

    @Override
    public CreditAccountDTO create(CreditAccountDTO creditAccountDTO) {
        if (creditAccountDTO.getIban() != null && creditAccountRepository.findById(creditAccountDTO.getIban()).isPresent()) {
            throw new IllegalArgumentException("Credit account already exists");
        }

        var primaryOwner = accountHolderService.findOwnerById(creditAccountDTO.getPrimaryOwner());
        var secondaryOwner = accountHolderService.findOwnerById(creditAccountDTO.getSecondaryOwner());
        var creditAccount = CreditAccount.fromDTO(creditAccountDTO, primaryOwner, secondaryOwner);
        creditAccount = creditAccountRepository.save(creditAccount);
        return CreditAccountDTO.fromEntity(creditAccount);
    }

    @Override
    public CreditAccountDTO findByIban(String iban) {
        var creditAccount = creditAccountRepository.findById(iban).orElseThrow(() -> new IllegalArgumentException("Credit account not found"));
        return CreditAccountDTO.fromEntity(creditAccount);
    }

    @Override
    public List<CreditAccountDTO> findAll() {
        var creditAccounts = creditAccountRepository.findAll();
        return CreditAccountDTO.fromList(creditAccounts);
    }

    @Override
    public CreditAccountDTO update(String iban, CreditAccountDTO creditAccountDTO) {
        return null;
    }

    @Override
    public void delete(String iban) {

    }
}
