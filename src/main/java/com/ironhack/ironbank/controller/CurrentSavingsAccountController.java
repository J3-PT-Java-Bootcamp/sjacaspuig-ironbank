package com.ironhack.ironbank.controller;

import com.ironhack.ironbank.dto.CurrentSavingsAccountDTO;
import com.ironhack.ironbank.service.CurrentSavingsAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/saving-accounts")
@RequiredArgsConstructor
public class CurrentSavingsAccountController {

    private final CurrentSavingsAccountService currentSavingsAccountService;

    @GetMapping
    public ResponseEntity<List<CurrentSavingsAccountDTO>> findAll() {
        return ResponseEntity.ok(currentSavingsAccountService.findAll());
    }

    @GetMapping("/{iban}")
    public ResponseEntity<CurrentSavingsAccountDTO> findById(@PathVariable @Valid String iban) {
        return ResponseEntity.ok(currentSavingsAccountService.findByIban(iban));
    }

    @PostMapping
    public ResponseEntity<CurrentSavingsAccountDTO> create(@RequestBody @Valid CurrentSavingsAccountDTO currentSavingsAccountDTO) {
        return ResponseEntity.ok(currentSavingsAccountService.create(currentSavingsAccountDTO));
    }

    @PutMapping("/{iban}")
    public ResponseEntity<CurrentSavingsAccountDTO> update(@PathVariable @Valid String iban, @RequestBody @Valid CurrentSavingsAccountDTO currentSavingsAccountDTO) {
        return ResponseEntity.ok(currentSavingsAccountService.update(iban, currentSavingsAccountDTO));
    }

    @DeleteMapping("/{iban}")
    public ResponseEntity<Void> delete(@PathVariable @Valid String iban) {
        currentSavingsAccountService.delete(iban);
        return ResponseEntity.noContent().build();
    }
}
