package com.ironhack.ironbank.service;

import com.ironhack.ironbank.dto.response.ThirdPartyCreateResponse;
import com.ironhack.ironbank.dto.ThirdPartyDTO;
import com.ironhack.ironbank.model.user.ThirdParty;
import com.ironhack.ironbank.repository.ThirdPartyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class ThirdPartyServiceImpl implements ThirdPartyService {

    private final ThirdPartyRepository thirdPartyRepository;

    @Override
    public ThirdPartyCreateResponse create(ThirdPartyDTO thirdPartyDTO) {
        var response = new ThirdPartyCreateResponse();
        if (thirdPartyDTO.getId() != null && thirdPartyRepository.findById(thirdPartyDTO.getId()).isPresent()) {
            response.setStatus(409);
            response.setMessage("User already exists");
            return response;
        }

        if(thirdPartyDTO.getId() == null) {
            thirdPartyDTO.setId(UUID.randomUUID().toString());
        }

        var thirdParty = ThirdParty.fromDTO(thirdPartyDTO);
        thirdParty = thirdPartyRepository.save(thirdParty);
        response.setStatus(201);
        response.setThirdParty(ThirdPartyDTO.fromEntity(thirdParty));
        return response;
    }

    @Override
    public ThirdPartyDTO findById(String id) {
        var thirdParty = thirdPartyRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Third party not found"));
        return ThirdPartyDTO.fromEntity(thirdParty);
    }

    @Override
    public List<ThirdPartyDTO> findAll() {
        var thirdParties = thirdPartyRepository.findAll();
        return ThirdPartyDTO.fromEntities(thirdParties);
    }

    @Override
    public ThirdPartyDTO update(String id, ThirdPartyDTO thirdPartyDTO) {
        var thirdParty = thirdPartyRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Third party not found"));
        var thirdPartyUpdated = ThirdParty.fromDTO(thirdPartyDTO);
        thirdPartyUpdated.setId(thirdParty.getId());
        thirdPartyUpdated = thirdPartyRepository.save(thirdPartyUpdated);
        return ThirdPartyDTO.fromEntity(thirdPartyUpdated);
    }

    @Override
    public void delete(String id) {
        thirdPartyRepository.deleteById(id);
    }
}
