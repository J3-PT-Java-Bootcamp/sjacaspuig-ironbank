package com.ironhack.ironbank.controller;

import com.ironhack.ironbank.dto.CurrentSavingsAccountDTO;
import com.ironhack.ironbank.service.CurrentSavingsAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/saving-accounts")
@RequiredArgsConstructor
@CrossOrigin
public class CurrentSavingsAccountController {

    private final CurrentSavingsAccountService currentSavingsAccountService;

    @GetMapping
    @RolesAllowed("backend-admin")
    public ResponseEntity<List<CurrentSavingsAccountDTO>> findAll() {
        return ResponseEntity.ok(currentSavingsAccountService.findAll());
    }

    @GetMapping("/{iban}")
    @RolesAllowed({"backend-admin", "backend-user"})
    public ResponseEntity<CurrentSavingsAccountDTO> findById(@PathVariable @Valid String iban) {
        return ResponseEntity.ok(currentSavingsAccountService.findByIban(iban));
    }

    @PostMapping
    @RolesAllowed("backend-admin")
    public ResponseEntity<CurrentSavingsAccountDTO> create(@RequestBody @Valid CurrentSavingsAccountDTO currentSavingsAccountDTO) {
        return ResponseEntity.ok(currentSavingsAccountService.create(currentSavingsAccountDTO));
    }

    @PutMapping("/{iban}")
    @RolesAllowed("backend-admin")
    public ResponseEntity<CurrentSavingsAccountDTO> update(@PathVariable @Valid String iban, @RequestBody @Valid CurrentSavingsAccountDTO currentSavingsAccountDTO) {
        return ResponseEntity.ok(currentSavingsAccountService.update(iban, currentSavingsAccountDTO));
    }

    @DeleteMapping("/{iban}")
    @RolesAllowed("backend-admin")
    public ResponseEntity<Void> delete(@PathVariable @Valid String iban) {
        currentSavingsAccountService.delete(iban);
        return ResponseEntity.noContent().build();
    }
}
