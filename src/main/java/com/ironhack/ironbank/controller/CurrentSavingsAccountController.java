package com.ironhack.ironbank.controller;

import com.ironhack.ironbank.service.CurrentSavingsAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/saving-accounts")
@RequiredArgsConstructor
public class CurrentSavingsAccountController {

    private final CurrentSavingsAccountService currentSavingsAccountService;
}
