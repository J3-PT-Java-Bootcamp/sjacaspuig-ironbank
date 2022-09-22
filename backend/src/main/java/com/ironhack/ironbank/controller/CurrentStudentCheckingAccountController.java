package com.ironhack.ironbank.controller;

import com.ironhack.ironbank.dto.CurrentStudentCheckingAccountDTO;
import com.ironhack.ironbank.service.CurrentStudentCheckingAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/student-checking-accounts")
@RequiredArgsConstructor
@CrossOrigin
public class CurrentStudentCheckingAccountController {

    private final CurrentStudentCheckingAccountService currentStudentCheckingAccountService;

    @GetMapping
    @RolesAllowed("backend-admin")
    public ResponseEntity<List<CurrentStudentCheckingAccountDTO>> findAll() {
        return ResponseEntity.ok(currentStudentCheckingAccountService.findAll());
    }

    @GetMapping("/{iban}")
    @RolesAllowed({"backend-admin", "backend-user"})
    public ResponseEntity<CurrentStudentCheckingAccountDTO> findById(@PathVariable @Valid String iban) {
        return ResponseEntity.ok(currentStudentCheckingAccountService.findByIban(iban));
    }

    @PostMapping
    @RolesAllowed("backend-admin")
    public ResponseEntity<CurrentStudentCheckingAccountDTO> create(@RequestBody @Valid CurrentStudentCheckingAccountDTO currentStudentCheckingAccountDTO) {
        return ResponseEntity.ok(currentStudentCheckingAccountService.create(currentStudentCheckingAccountDTO));
    }

    @PutMapping("/{iban}")
    @RolesAllowed("backend-admin")
    public ResponseEntity<CurrentStudentCheckingAccountDTO> update(@PathVariable @Valid String iban, @RequestBody @Valid CurrentStudentCheckingAccountDTO currentStudentCheckingAccountDTO) {
        return ResponseEntity.ok(currentStudentCheckingAccountService.update(iban, currentStudentCheckingAccountDTO));
    }

    @DeleteMapping("/{iban}")
    @RolesAllowed("backend-admin")
    public ResponseEntity<Void> delete(@PathVariable @Valid String iban) {
        currentStudentCheckingAccountService.delete(iban);
        return ResponseEntity.noContent().build();
    }
}
