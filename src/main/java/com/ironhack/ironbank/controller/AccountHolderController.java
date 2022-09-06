package com.ironhack.ironbank.controller;

import com.ironhack.ironbank.dto.AccountHolderDTO;
import com.ironhack.ironbank.service.AccountHolderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/account-holders")
@RequiredArgsConstructor
public class AccountHolderController {

    private final AccountHolderService accountHolderService;

    @GetMapping
    public ResponseEntity<List<AccountHolderDTO>> findAll() {
        return ResponseEntity.ok(accountHolderService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountHolderDTO> findById(@PathVariable @Valid Long id) {
        return ResponseEntity.ok(accountHolderService.findById(id));
    }

    @PostMapping
    public ResponseEntity<AccountHolderDTO> create(@RequestBody @Valid AccountHolderDTO accountHoldertDTO) {
        return ResponseEntity.ok(accountHolderService.create(accountHoldertDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AccountHolderDTO> update(@PathVariable @Valid Long id, @RequestBody @Valid AccountHolderDTO accountHoldertDTO) {
        return ResponseEntity.ok(accountHolderService.update(id, accountHoldertDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable @Valid Long id) {
        accountHolderService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
