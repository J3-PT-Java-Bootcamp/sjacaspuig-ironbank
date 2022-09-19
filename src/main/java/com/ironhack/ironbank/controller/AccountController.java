package com.ironhack.ironbank.controller;

import com.ironhack.ironbank.dto.AccountDTO;
import com.ironhack.ironbank.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/accounts")
@RequiredArgsConstructor
@CrossOrigin
public class AccountController {

    private final AccountService accountService;

    @GetMapping
    @RolesAllowed("backend-admin")
    public ResponseEntity<List<AccountDTO>> findAll() {
        return ResponseEntity.ok(accountService.findAll());
    }

    @GetMapping("/{iban}")
    @RolesAllowed("backend-admin")
    public ResponseEntity<AccountDTO> findById(@PathVariable @Valid String iban) {
        return ResponseEntity.ok(accountService.findByIban(iban));
    }

    @DeleteMapping("/{iban}")
    @RolesAllowed("backend-admin")
    public ResponseEntity<Void> delete(@PathVariable @Valid String iban) {
        accountService.delete(iban);
        return ResponseEntity.noContent().build();
    }
}
