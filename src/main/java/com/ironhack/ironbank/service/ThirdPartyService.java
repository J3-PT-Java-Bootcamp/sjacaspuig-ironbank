package com.ironhack.ironbank.service;

import com.ironhack.ironbank.dto.ThirdPartyDTO;

import javax.validation.Valid;
import java.util.List;
public interface ThirdPartyService {

    ThirdPartyDTO create(@Valid ThirdPartyDTO thirdPartyDTO);
    ThirdPartyDTO findById(@Valid Long id);
    List<ThirdPartyDTO> findAll();
    ThirdPartyDTO update(@Valid Long id, @Valid ThirdPartyDTO thirdPartyDTO);
    void delete(@Valid Long id);
}
