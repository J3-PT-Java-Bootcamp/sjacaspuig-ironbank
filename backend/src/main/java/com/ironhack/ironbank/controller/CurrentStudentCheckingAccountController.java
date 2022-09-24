package com.ironhack.ironbank.controller;

import com.ironhack.ironbank.dto.CurrentStudentCheckingAccountDTO;
import com.ironhack.ironbank.service.CurrentStudentCheckingAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/student-checking-accounts")
@RequiredArgsConstructor
@CrossOrigin
public class CurrentStudentCheckingAccountController {

    private final CurrentStudentCheckingAccountService currentStudentCheckingAccountService;

    @GetMapping
    public ResponseEntity<List<CurrentStudentCheckingAccountDTO>> findAll() {
        return ResponseEntity.ok(currentStudentCheckingAccountService.findAll());
    }

    @GetMapping("/{iban}")
    public ResponseEntity<CurrentStudentCheckingAccountDTO> findById(@PathVariable @Valid String iban) {
        return ResponseEntity.ok(currentStudentCheckingAccountService.findByIban(iban));
    }

    @PostMapping
    public ResponseEntity<CurrentStudentCheckingAccountDTO> create(@RequestBody @Valid CurrentStudentCheckingAccountDTO currentStudentCheckingAccountDTO) {
        return ResponseEntity.created(null).body(currentStudentCheckingAccountService.create(currentStudentCheckingAccountDTO));
    }

    @PutMapping("/{iban}")
    public ResponseEntity<CurrentStudentCheckingAccountDTO> update(@PathVariable @Valid String iban, @RequestBody @Valid CurrentStudentCheckingAccountDTO currentStudentCheckingAccountDTO) {
        return ResponseEntity.ok(currentStudentCheckingAccountService.update(iban, currentStudentCheckingAccountDTO));
    }

    @DeleteMapping("/{iban}")
    public ResponseEntity<Void> delete(@PathVariable @Valid String iban) {
        currentStudentCheckingAccountService.delete(iban);
        return ResponseEntity.noContent().build();
    }
}
