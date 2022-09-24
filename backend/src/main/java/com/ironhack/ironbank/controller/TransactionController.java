package com.ironhack.ironbank.controller;

import com.ironhack.ironbank.dto.TransactionDTO;
import com.ironhack.ironbank.enums.TransactionStatus;
import com.ironhack.ironbank.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/transactions")
@RequiredArgsConstructor
@CrossOrigin
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping
    @RolesAllowed("backend-admin")
    public ResponseEntity<List<TransactionDTO>> findAll() {
        return ResponseEntity.ok(transactionService.findAll());
    }

    @GetMapping("/{id}")
    @RolesAllowed({"backend-admin", "backend-user"})
    public ResponseEntity<TransactionDTO> findById(@PathVariable @Valid Long id) {
        return ResponseEntity.ok(transactionService.findById(id));
    }

    @PostMapping
    @RolesAllowed("backend-admin")
    public ResponseEntity<TransactionDTO> create(@RequestBody @Valid TransactionDTO transactionDTO, Principal principal) {

        var transactionResponse = transactionService.create(transactionDTO);

        if (transactionResponse.getStatus().equals(TransactionStatus.COMPLETED)) {
            return ResponseEntity.created(null).body(transactionResponse);
        } else {
            return ResponseEntity.badRequest().body(transactionResponse);
        }
    }

    // Get transaction by iban
    @GetMapping("/iban/{iban}")
    @RolesAllowed("backend-user")
    public ResponseEntity<List<TransactionDTO>> findByIban(@PathVariable @Valid String iban) {
        return ResponseEntity.ok(transactionService.findByIban(iban));
    }

    @GetMapping("/account-holder/{id}")
    @RolesAllowed("backend-user")
    public ResponseEntity<List<TransactionDTO>> findByAccountHolderId(@PathVariable @Valid String id) {
        return ResponseEntity.ok(transactionService.findByAccountHolderId(id));
    }
}
