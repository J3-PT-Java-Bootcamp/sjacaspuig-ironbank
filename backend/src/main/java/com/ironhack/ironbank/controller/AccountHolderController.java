package com.ironhack.ironbank.controller;

import com.ironhack.ironbank.dto.AccountHolderDTO;
import com.ironhack.ironbank.service.AccountHolderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/account-holders")
@RequiredArgsConstructor
@CrossOrigin
public class AccountHolderController {

    private final AccountHolderService accountHolderService;

    @GetMapping
    public ResponseEntity<List<AccountHolderDTO>> findAll() {
        return ResponseEntity.ok(accountHolderService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountHolderDTO> findById(@PathVariable String id) {
        return ResponseEntity.ok(accountHolderService.findById(id));
    }

    @PostMapping
    public ResponseEntity<AccountHolderDTO> create(@RequestBody @Valid AccountHolderDTO accountHolderDTO) {
        var response = accountHolderService.create(accountHolderDTO);

        if (response.getStatus() == 201) {
            return ResponseEntity.status(201).body(response.getAccountHolder());
        } else if (response.getStatus() == 409) {
            return ResponseEntity.status(409).body(response.getAccountHolder());
        } else {
            return ResponseEntity.status(500).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<AccountHolderDTO> update(@PathVariable @Valid String id, @RequestBody @Valid AccountHolderDTO accountHoldertDTO) {
        return ResponseEntity.ok(accountHolderService.update(id, accountHoldertDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable @Valid String id) {
        accountHolderService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
