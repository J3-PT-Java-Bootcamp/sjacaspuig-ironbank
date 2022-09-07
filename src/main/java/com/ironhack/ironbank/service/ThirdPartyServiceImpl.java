package com.ironhack.ironbank.service;

import com.ironhack.ironbank.dto.ThirdPartyDTO;
import com.ironhack.ironbank.model.user.ThirdParty;
import com.ironhack.ironbank.repository.ThirdPartyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ThirdPartyServiceImpl implements ThirdPartyService {

    private final ThirdPartyRepository thirdPartyRepository;

    @Override
    public ThirdPartyDTO create(ThirdPartyDTO thirdPartyDTO) {
        if (thirdPartyDTO.getId() != null && thirdPartyRepository.findById(thirdPartyDTO.getId()).isPresent()) {
            throw new IllegalArgumentException("Third party already exists");
        }

        var thirdParty = ThirdParty.fromDTO(thirdPartyDTO);
        thirdParty = thirdPartyRepository.save(thirdParty);
        return ThirdPartyDTO.fromEntity(thirdParty);
    }

    @Override
    public ThirdPartyDTO findById(Long id) {
        var thirdParty = thirdPartyRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Third party not found"));
        return ThirdPartyDTO.fromEntity(thirdParty);
    }

    @Override
    public List<ThirdPartyDTO> findAll() {
        var thirdParties = thirdPartyRepository.findAll();
        return ThirdPartyDTO.fromEntities(thirdParties);
    }

    @Override
    public ThirdPartyDTO update(Long id, ThirdPartyDTO thirdPartyDTO) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }
}
