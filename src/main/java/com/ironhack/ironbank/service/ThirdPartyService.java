package com.ironhack.ironbank.service;

import com.ironhack.ironbank.dto.ThirdPartyDTO;

import java.util.List;
public interface ThirdPartyService {

    ThirdPartyDTO create(ThirdPartyDTO thirdPartyDTO);
    ThirdPartyDTO findById(Long id);
    List<ThirdPartyDTO> findAll();
    ThirdPartyDTO update(Long id, ThirdPartyDTO thirdPartyDTO);
    void delete(Long id);
}
