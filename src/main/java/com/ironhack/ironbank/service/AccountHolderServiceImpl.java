package com.ironhack.ironbank.service;

import com.ironhack.ironbank.dto.AccountHolderDTO;
import com.ironhack.ironbank.model.user.AccountHolder;
import com.ironhack.ironbank.repository.AccountHolderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountHolderServiceImpl implements AccountHolderService {

    private final AccountHolderRepository accountHolderRepository;

    @Override
    public AccountHolderDTO create(AccountHolderDTO accountHolderDTO) {
        if(accountHolderDTO.getId() != null && accountHolderRepository.findById(accountHolderDTO.getId()).isPresent()) {
            throw new IllegalArgumentException("Account holder already exists");
        }

        var accountHolder = AccountHolder.fromDTO(accountHolderDTO);
        accountHolder = accountHolderRepository.save(accountHolder);
        return AccountHolderDTO.fromEntity(accountHolder);
    }

    @Override
    public AccountHolderDTO findById(Long id) {
        var accountHolder = accountHolderRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Account holder not found"));
        return AccountHolderDTO.fromEntity(accountHolder);
    }

    @Override
    public List<AccountHolderDTO> findAll() {
        var accountHolders = accountHolderRepository.findAll();
        return AccountHolderDTO.fromEntities(accountHolders);
    }

    @Override
    public AccountHolderDTO update(Long id, AccountHolderDTO accountHolderDTO) {
        var accountHolder = accountHolderRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Account holder not found"));
        var accountHolderUpdated = AccountHolder.fromDTO(accountHolderDTO);
        accountHolderUpdated.setId(accountHolder.getId());
        accountHolderUpdated = accountHolderRepository.save(accountHolderUpdated);
        return AccountHolderDTO.fromEntity(accountHolderUpdated);
    }

    @Override
    public void delete(Long id) {
        accountHolderRepository.deleteById(id);
    }

    @Override
    public AccountHolder findOwnerById(Long id) {
        if (id != null) {
            if (accountHolderRepository.findById(id).isPresent()) {
                return accountHolderRepository.findById(id).get();
            } else {
                return null;
            }
        } else {
            return null;
        }
    }
}
