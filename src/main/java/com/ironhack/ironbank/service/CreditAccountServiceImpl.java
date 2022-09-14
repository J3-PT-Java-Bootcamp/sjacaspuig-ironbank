package com.ironhack.ironbank.service;

import com.ironhack.ironbank.dto.CreditAccountDTO;
import com.ironhack.ironbank.model.account.CreditAccount;
import com.ironhack.ironbank.repository.CreditAccountRepository;
import com.ironhack.ironbank.utils.IbanGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CreditAccountServiceImpl implements CreditAccountService {

    private final CreditAccountRepository creditAccountRepository;
    private final AccountHolderService accountHolderService;
    private final AccountService accountService;

    @Override
    public CreditAccountDTO create(CreditAccountDTO creditAccountDTO) {
        if (creditAccountDTO.getIban() != null && creditAccountRepository.findById(creditAccountDTO.getIban()).isPresent()) {
            throw new IllegalArgumentException("Credit account already exists");
        }
        if (creditAccountDTO.getBalance().getAmount().compareTo(new BigDecimal("0")) < 0) {
            throw new IllegalArgumentException("Balance cannot be negative");
        }

        // Generate iban, check if it exists on accounts, if it does, generate another one, if not, save it
        String iban = new IbanGenerator().generateIban();
        while (accountService.findById(iban).isPresent()) {
            iban = new IbanGenerator().generateIban();
        }
        creditAccountDTO.setIban(iban);

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
        var creditAccount = creditAccountRepository.findById(iban).orElseThrow(() -> new IllegalArgumentException("Credit account not found"));
        var creditAccountUpdated = CreditAccount.fromDTO(creditAccountDTO, creditAccount.getPrimaryOwner(), creditAccount.getSecondaryOwner());
        creditAccountUpdated.setIban(creditAccount.getIban());
        creditAccountUpdated = creditAccountRepository.save(creditAccountUpdated);
        return CreditAccountDTO.fromEntity(creditAccountUpdated);
    }

    @Override
    public void delete(String iban) {
        creditAccountRepository.deleteById(iban);
    }
}
