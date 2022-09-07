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
        return null;
    }

    @Override
    public List<AccountHolderDTO> findAll() {
        return null;
    }

    @Override
    public AccountHolderDTO update(Long id, AccountHolderDTO accountHolderDTO) {
        return null;
    }

    @Override
    public void delete(Long id) {

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
