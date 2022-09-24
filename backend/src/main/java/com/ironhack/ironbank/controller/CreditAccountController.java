package com.ironhack.ironbank.controller;

import com.ironhack.ironbank.dto.CreditAccountDTO;
import com.ironhack.ironbank.service.CreditAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/credit-accounts")
@RequiredArgsConstructor
@CrossOrigin
public class CreditAccountController {

    private final CreditAccountService creditAccountService;

    @GetMapping
    @RolesAllowed("backend-admin")
    public ResponseEntity<List<CreditAccountDTO>> findAll() {
        return ResponseEntity.ok(creditAccountService.findAll());
    }

    @GetMapping("/{iban}")
    @RolesAllowed({"backend-admin", "backend-user"})
    public ResponseEntity<CreditAccountDTO> findById(@PathVariable @Valid String iban) {
        return ResponseEntity.ok(creditAccountService.findByIban(iban));
    }

    @PostMapping
    @RolesAllowed("backend-admin")
    public ResponseEntity<CreditAccountDTO> create(@RequestBody @Valid CreditAccountDTO creditAccountDTO) {
        return ResponseEntity.created(null).body(creditAccountService.create(creditAccountDTO));
    }

    @PutMapping("/{iban}")
    @RolesAllowed("backend-admin")
    public ResponseEntity<CreditAccountDTO> update(@PathVariable @Valid String iban, @RequestBody @Valid CreditAccountDTO creditAccountDTO) {
        return ResponseEntity.ok(creditAccountService.update(iban, creditAccountDTO));
    }

    @DeleteMapping("/{iban}")
    @RolesAllowed("backend-admin")
    public ResponseEntity<Void> delete(@PathVariable @Valid String iban) {
        creditAccountService.delete(iban);
        return ResponseEntity.noContent().build();
    }
}
