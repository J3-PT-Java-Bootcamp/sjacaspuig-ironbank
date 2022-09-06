package com.ironhack.ironbank.controller;

import com.ironhack.ironbank.service.CurrentCheckingAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/checking-accounts")
@RequiredArgsConstructor
public class CurrentCheckingAccountController {

    private final CurrentCheckingAccountService currentCheckingAccountService;
}
