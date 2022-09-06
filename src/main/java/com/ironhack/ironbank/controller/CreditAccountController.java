package com.ironhack.ironbank.controller;

import com.ironhack.ironbank.service.CreditAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/credit-accounts")
@RequiredArgsConstructor
public class CreditAccountController {

    private final CreditAccountService creditAccountService;
}
