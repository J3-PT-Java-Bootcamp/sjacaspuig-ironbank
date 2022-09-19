package com.ironhack.ironbank.controller;

import com.ironhack.ironbank.dto.TransactionDTO;
import com.ironhack.ironbank.service.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
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
    public ResponseEntity<TransactionDTO> create(@RequestBody @Valid TransactionDTO transactionDTO) {
        var transactionResponse = transactionService.create(transactionDTO);

        if (transactionResponse.getStatus().equals("SUCCESS")) {
            return ResponseEntity.ok(transactionDTO);
        } else {
            return ResponseEntity.badRequest().body(transactionDTO);
        }
    }
}
