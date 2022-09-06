package com.ironhack.ironbank.controller;

import com.ironhack.ironbank.dto.AdminDTO;
import com.ironhack.ironbank.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@Controller
@RequestMapping("/admins")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping
    public ResponseEntity<List<AdminDTO>> findAll() {
        return ResponseEntity.ok(adminService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdminDTO> findById(@PathVariable @Valid Long id) {
        return ResponseEntity.ok(adminService.findById(id));
    }

    @PostMapping
    public ResponseEntity<AdminDTO> create(@RequestBody @Valid AdminDTO adminDTO) {
        return ResponseEntity.ok(adminService.create(adminDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AdminDTO> update(@PathVariable @Valid Long id, @RequestBody @Valid AdminDTO adminDTO) {
        return ResponseEntity.ok(adminService.update(id, adminDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable @Valid Long id) {
        adminService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
