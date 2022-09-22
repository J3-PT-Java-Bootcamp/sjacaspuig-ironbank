package com.ironhack.ironbank.service;

import com.ironhack.ironbank.dto.response.ThirdPartyCreateResponse;
import com.ironhack.ironbank.dto.ThirdPartyDTO;

import javax.validation.Valid;
import java.util.List;


public interface ThirdPartyService {

    ThirdPartyCreateResponse create(@Valid ThirdPartyDTO thirdPartyDTO);
    ThirdPartyDTO findById(@Valid String id);
    List<ThirdPartyDTO> findAll();
    ThirdPartyDTO update(@Valid String id, @Valid ThirdPartyDTO thirdPartyDTO);
    void delete(@Valid String id);
}
