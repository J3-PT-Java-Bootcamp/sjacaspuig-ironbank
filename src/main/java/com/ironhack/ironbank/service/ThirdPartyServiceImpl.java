package com.ironhack.ironbank.service;

import com.ironhack.ironbank.dto.ThirdPartyDTO;
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
        return null;
    }

    @Override
    public ThirdPartyDTO findById(Long id) {
        return null;
    }

    @Override
    public List<ThirdPartyDTO> findAll() {
        return null;
    }

    @Override
    public ThirdPartyDTO update(Long id, ThirdPartyDTO thirdPartyDTO) {
        return null;
    }

    @Override
    public void delete(Long id) {

    }
}
