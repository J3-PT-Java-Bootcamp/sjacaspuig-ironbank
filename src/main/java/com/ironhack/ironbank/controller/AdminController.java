package com.ironhack.ironbank.controller;

import com.ironhack.ironbank.dto.AdminDTO;
import com.ironhack.ironbank.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/admins")
@RequiredArgsConstructor
@CrossOrigin
public class AdminController {

    private final AdminService adminService;

    @PostMapping()
    @RolesAllowed("backend-admin")
    public ResponseEntity<AdminDTO> create(@RequestBody @Valid AdminDTO adminDTO) {
        var admin = adminService.create(adminDTO);

        if (admin.getStatus() == 201) {
            return ResponseEntity.status(201).body(admin.getAdmin());
        } else if (admin.getStatus() == 409) {
            return ResponseEntity.status(409).body(admin.getAdmin());
        } else {
            return ResponseEntity.status(500).build();
        }
    }

    @GetMapping
    public ResponseEntity<List<AdminDTO>> findAll() {
        return ResponseEntity.ok(adminService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdminDTO> findById(@PathVariable @Valid String id) {
        return ResponseEntity.ok(adminService.findById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AdminDTO> update(@PathVariable @Valid String id, @RequestBody @Valid AdminDTO adminDTO) {
        return ResponseEntity.ok(adminService.update(id, adminDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable @Valid String id) {
        adminService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
