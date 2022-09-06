package com.ironhack.ironbank.service;

import com.ironhack.ironbank.repository.ThirdPartyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ThirdPartyServiceImpl implements ThirdPartyService {

    private final ThirdPartyRepository thirdPartyRepository;
}
