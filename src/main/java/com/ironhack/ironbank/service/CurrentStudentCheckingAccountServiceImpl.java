package com.ironhack.ironbank.service;

import com.ironhack.ironbank.repository.CurrentStudentCheckingAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CurrentStudentCheckingAccountServiceImpl implements CurrentStudentCheckingAccountService {

    private final CurrentStudentCheckingAccountRepository currentStudentCheckingAccountRepository;
}
