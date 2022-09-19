package com.ironhack.ironbank.controller;

import com.ironhack.ironbank.dto.CurrentCheckingAccountDTO;
import com.ironhack.ironbank.service.CurrentCheckingAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/checking-accounts")
@RequiredArgsConstructor
@CrossOrigin
public class CurrentCheckingAccountController {

    private final CurrentCheckingAccountService currentCheckingAccountService;

    @GetMapping
    @RolesAllowed("backend-admin")
    public ResponseEntity<List<CurrentCheckingAccountDTO>> findAll() {
        return ResponseEntity.ok(currentCheckingAccountService.findAll());
    }

    @GetMapping("/{iban}")
    @RolesAllowed({"backend-admin", "backend-user"})
    public ResponseEntity<CurrentCheckingAccountDTO> findById(@PathVariable @Valid String iban) {
        return ResponseEntity.ok(currentCheckingAccountService.findByIban(iban));
    }

    @PostMapping
    @RolesAllowed("backend-admin")
    public ResponseEntity<CurrentCheckingAccountDTO> create(@RequestBody @Valid CurrentCheckingAccountDTO currentCheckingAccountDTO) {
        return ResponseEntity.ok(currentCheckingAccountService.create(currentCheckingAccountDTO));
    }

    @PutMapping("/{iban}")
    @RolesAllowed("backend-admin")
    public ResponseEntity<CurrentCheckingAccountDTO> update(@PathVariable @Valid String iban, @RequestBody @Valid CurrentCheckingAccountDTO currentCheckingAccountDTO) {
        return ResponseEntity.ok(currentCheckingAccountService.update(iban, currentCheckingAccountDTO));
    }

    @DeleteMapping("/{iban}")
    @RolesAllowed("backend-admin")
    public ResponseEntity<Void> delete(@PathVariable @Valid String iban) {
        currentCheckingAccountService.delete(iban);
        return ResponseEntity.noContent().build();
    }
}
