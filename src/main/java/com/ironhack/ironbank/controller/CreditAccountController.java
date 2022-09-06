package com.ironhack.ironbank.controller;

import com.ironhack.ironbank.dto.CreditAccountDTO;
import com.ironhack.ironbank.service.CreditAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/credit-accounts")
@RequiredArgsConstructor
public class CreditAccountController {

    private final CreditAccountService creditAccountService;

    @GetMapping
    public ResponseEntity<List<CreditAccountDTO>> findAll() {
        return ResponseEntity.ok(creditAccountService.findAll());
    }

    @GetMapping("/{iban}")
    public ResponseEntity<CreditAccountDTO> findById(@PathVariable @Valid String iban) {
        return ResponseEntity.ok(creditAccountService.findByIban(iban));
    }

    @PostMapping
    public ResponseEntity<CreditAccountDTO> create(@RequestBody @Valid CreditAccountDTO creditAccountDTO) {
        return ResponseEntity.ok(creditAccountService.create(creditAccountDTO));
    }

    @PutMapping("/{iban}")
    public ResponseEntity<CreditAccountDTO> update(@PathVariable @Valid String iban, @RequestBody @Valid CreditAccountDTO creditAccountDTO) {
        return ResponseEntity.ok(creditAccountService.update(iban, creditAccountDTO));
    }

    @DeleteMapping("/{iban}")
    public ResponseEntity<Void> delete(@PathVariable @Valid String iban) {
        creditAccountService.delete(iban);
        return ResponseEntity.noContent().build();
    }
}
