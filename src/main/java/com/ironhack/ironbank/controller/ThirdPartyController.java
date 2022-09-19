package com.ironhack.ironbank.controller;

import com.ironhack.ironbank.dto.ThirdPartyDTO;
import com.ironhack.ironbank.service.ThirdPartyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.util.List;


@RestController
@RequestMapping("/third-parties")
@RequiredArgsConstructor
@CrossOrigin
public class ThirdPartyController {

    private final ThirdPartyService thirdPartyService;

    @GetMapping
    @RolesAllowed("backend-admin")
    public ResponseEntity<List<ThirdPartyDTO>> findAll() {
        return ResponseEntity.ok(thirdPartyService.findAll());
    }

    @GetMapping("/{id}")
    @RolesAllowed("backend-admin")
    public ResponseEntity<ThirdPartyDTO> findById(@PathVariable @Valid String id) {
        return ResponseEntity.ok(thirdPartyService.findById(id));
    }

    @PostMapping
    @RolesAllowed("backend-admin")
    public ResponseEntity<ThirdPartyDTO> create(@RequestBody @Valid ThirdPartyDTO thirdPartyDTO) {
        var response = thirdPartyService.create(thirdPartyDTO);
        if (response.getStatus() == 201) {
            return ResponseEntity.status(201).body(response.getThirdParty());
        } else if (response.getStatus() == 409) {
            return ResponseEntity.status(409).body(response.getThirdParty());
        } else {
            return ResponseEntity.status(500).build();
        }
    }

    @PutMapping("/{id}")
    @RolesAllowed("backend-admin")
    public ResponseEntity<ThirdPartyDTO> update(@PathVariable @Valid String id, @RequestBody @Valid ThirdPartyDTO thirdPartyDTO) {
        return ResponseEntity.ok(thirdPartyService.update(id, thirdPartyDTO));
    }

    @DeleteMapping("/{id}")
    @RolesAllowed("backend-admin")
    public ResponseEntity<Void> delete(@PathVariable @Valid String id) {
        thirdPartyService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
