package com.ironhack.ironbank.service;

import com.ironhack.ironbank.dto.response.AccountHolderCreateResponse;
import com.ironhack.ironbank.dto.AccountHolderDTO;
import com.ironhack.ironbank.dto.UserSecurityDTO;
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
    public AccountHolderCreateResponse create(AccountHolderDTO accountHolderDTO) {
        var response = new AccountHolderCreateResponse();
        if(accountHolderDTO.getId() != null && accountHolderRepository.findById(accountHolderDTO.getId()).isPresent()) {
            response.setStatus(409);
            response.setMessage("User already exists");
            return response;
        }

        var userSecurityDTO = UserSecurityDTO.fromDTO(accountHolderDTO);
        var serviceResponse = securityService.createUser(userSecurityDTO, RealmGroup.USERS);
        response.setStatus(serviceResponse.getStatus());
        UserSecurityDTO userSecurityDTOCreated = serviceResponse.getUser();

        if (response.getStatus() == 201) {
            var accountHolder = AccountHolder.fromDTO(accountHolderDTO);
            accountHolder.setId(userSecurityDTOCreated.getId());
            accountHolder = accountHolderRepository.save(accountHolder);
            response.setAccountHolder(AccountHolderDTO.fromEntity(accountHolder));
        } else if (response.getStatus() == 409) {
            var accountHolder = accountHolderRepository.findById(userSecurityDTOCreated.getId());

            if (accountHolder.isPresent()) {
                response.setAccountHolder(AccountHolderDTO.fromEntity(accountHolder.get()));
            } else {
                response.setStatus(500);
                response.setMessage("User exists on Keycloak but not in the database");
            }
        }

        return response;
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

        var userSecurityDTO = UserSecurityDTO.fromDTO(accountHolderDTO);
        securityService.updateUser(accountHolder.getId(), userSecurityDTO);

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
