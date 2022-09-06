package com.ironhack.ironbank.controller;

import com.ironhack.ironbank.service.AccountHolderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/account-holders")
@RequiredArgsConstructor
public class AccountHolderController {

    private final AccountHolderService accountHolderService;
}
