package com.ironhack.ironbank.service;

import com.ironhack.ironbank.dto.AccountHolderDTO;
import com.ironhack.ironbank.dto.UserKeycloakDTO;
import com.ironhack.ironbank.enums.RealmGroup;
import com.ironhack.ironbank.model.user.AccountHolder;
import com.ironhack.ironbank.repository.AccountHolderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class AccountHolderServiceImpl implements AccountHolderService {

    private final AccountHolderRepository accountHolderRepository;
    private final SecurityService securityService;


    @Override
    public AccountHolderDTO create(AccountHolderDTO accountHolderDTO, String password) {
        if(accountHolderDTO.getId() != null && accountHolderRepository.findById(accountHolderDTO.getId()).isPresent()) {
            throw new IllegalArgumentException("Account holder already exists");
        }

        var userKeycloakDTO = UserKeycloakDTO.fromDTO(accountHolderDTO, password);
        var serviceResponse = securityService.createUser(userKeycloakDTO, RealmGroup.USERS);
        var status = (Integer) serviceResponse[0];
        UserKeycloakDTO userKeycloakDTOCreated = (UserKeycloakDTO) serviceResponse[1];

        if (status == 201) {
            var accountHolder = AccountHolder.fromDTO(accountHolderDTO);
            accountHolder.setId(userKeycloakDTOCreated.getId());
            accountHolder = accountHolderRepository.save(accountHolder);
            return AccountHolderDTO.fromEntity(accountHolder);
        } else if (status == 409) {
            var accountHolder = accountHolderRepository.findById(userKeycloakDTOCreated.getId()).orElseThrow(() -> new IllegalArgumentException("Account holder not found"));
            return AccountHolderDTO.fromEntity(accountHolder);
        } else {
            throw new IllegalArgumentException("Error creating user");
        }
    }

    @Override
    public AccountHolderDTO findById(String id) {
        var accountHolder = accountHolderRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Account holder not found"));
        return AccountHolderDTO.fromEntity(accountHolder);
    }

    @Override
    public List<AccountHolderDTO> findAll() {
        var accountHolders = accountHolderRepository.findAll();
        return AccountHolderDTO.fromEntities(accountHolders);
    }

    @Override
    public AccountHolderDTO update(String id, AccountHolderDTO accountHolderDTO) {
        var accountHolder = accountHolderRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Account holder not found"));

        var userKeycloakDTO = UserKeycloakDTO.fromDTO(accountHolderDTO, null);
        securityService.updateUser(accountHolder.getId(), userKeycloakDTO);

        var accountHolderUpdated = AccountHolder.fromDTO(accountHolderDTO);
        accountHolderUpdated.setId(accountHolder.getId());
        accountHolderUpdated = accountHolderRepository.save(accountHolderUpdated);
        return AccountHolderDTO.fromEntity(accountHolderUpdated);
    }

    @Override
    public void delete(String id) {
        securityService.deleteUser(id);
        accountHolderRepository.deleteById(id);
    }

    @Override
    public AccountHolder findOwnerById(String id) {
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
