package com.ironhack.ironbank.controller;

import com.ironhack.ironbank.service.CurrentStudentCheckingAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/student-checking-accounts")
@RequiredArgsConstructor
public class CurrentStudentCheckingAccountController {

    private final CurrentStudentCheckingAccountService currentStudentCheckingAccountService;
}
